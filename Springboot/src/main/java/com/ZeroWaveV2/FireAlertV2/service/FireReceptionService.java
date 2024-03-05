package com.ZeroWaveV2.FireAlertV2.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ZeroWaveV2.FireAlertV2.dto.FireReceptionDto;
import com.ZeroWaveV2.FireAlertV2.model.FireReception;
import com.ZeroWaveV2.FireAlertV2.model.FireSituationRoom;
import com.ZeroWaveV2.FireAlertV2.model.FireStation;
import com.ZeroWaveV2.FireAlertV2.model.Result;
import com.ZeroWaveV2.FireAlertV2.model.User;
import com.ZeroWaveV2.FireAlertV2.repository.FireReceptionRepository;
import com.ZeroWaveV2.FireAlertV2.repository.FireSituationRoomRepository;
import com.ZeroWaveV2.FireAlertV2.repository.ResultRepository;
import com.ZeroWaveV2.FireAlertV2.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class FireReceptionService {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FireReceptionRepository fireReceptionRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${firealert.image.storage}")
	private String imageStoragePath;

	@Autowired
	private FlaskCommunicationService flaskCommunicationService; // FlaskCommunicationService 주입

	@Autowired
	private FireSituationRoomRepository fireSituationRoomRepository;
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
    private FireStationService fireStationService;
	
	@Autowired
	private ResultRepository resultRepository;
	
	@Autowired
    private ReverseGeocodingService reverseGeocodingService;

	public void processFireAlert(String userHp, MultipartFile image, String gps) throws IOException {
		Path directoryPath = Paths.get(imageStoragePath, userHp);
		if (!Files.exists(directoryPath)) {
			Files.createDirectories(directoryPath);
		}

		// 이미지 파일 이름에 UUID를 추가하여 중복 방지
		String originalFilename = image.getOriginalFilename();
		String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf("."))
				: "";
		String newFileName = UUID.randomUUID().toString() + fileExtension;
		Path imagePath = directoryPath.resolve(newFileName);

		Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

		FireReceptionDto fireReceptionDto = new FireReceptionDto(userHp, imagePath.toString(), gps);
		String message = objectMapper.writeValueAsString(fireReceptionDto);
		kafkaTemplate.send("firealert_topic_v13", message);
	}

	@KafkaListener(topics = "firealert_topic_v13", groupId = "${spring.kafka.consumer.group-id}")
	public void receiveFireAlertMessage(String message) throws IOException {
	
		FireReceptionDto fireReceptionDto = objectMapper.readValue(message, FireReceptionDto.class);

		User user = userRepository.findByHp(fireReceptionDto.getUserHp()).orElseThrow();

		FireReception fireReception = new FireReception();
		fireReception.setUser(user);
		fireReception.setImgurl(fireReceptionDto.getImgurl());
		fireReception.setGps(fireReceptionDto.getGps());
		String[] gpsCoordinates = fireReception.getGps().split(",");
        double latitude = Double.parseDouble(gpsCoordinates[0]);
        double longitude = Double.parseDouble(gpsCoordinates[1]);
        String address = reverseGeocodingService.getAddress(latitude, longitude);
		fireReception.setAddress(address);

		fireReceptionRepository.save(fireReception);

		// Flask 서버로 이미지를 재전송하여 결과 받기
		String flaskResponse = flaskCommunicationService.sendImageToFlaskAndReceiveResults(fireReception.getImgurl());

		try {
			JsonNode rootNode = objectMapper.readTree(flaskResponse);
			String detectedObject = rootNode.path("message").asText();
			
			// 가장 가까운 FireStation 찾기
	        FireStation nearestFireStation = fireStationService.findNearestFireStation(fireReception.getGps());

			// Flask 결과 값을 사용하여 FireSituationRoom 엔티티 생성 및 저장
			FireSituationRoom fireSituationRoom = new FireSituationRoom();
			
			// FireSituationRoom에 가장 가까운 FireStation 설정
	        fireSituationRoom.setFireStation(nearestFireStation);
			// FireReception 엔티티 참조 설정
			fireSituationRoom.setFireReception(fireReception);
			// Flask 결과 값을 state 필드에 설정
			fireSituationRoom.setState(detectedObject);
			// 추가된: num 정보 설정 (이 부분은 이미 FireReception 참조에 포함되어 있으므로 별도 설정 필요 없음)
			
			// 240303 수정
			//detectedObject가 "no object" 일 경우 fireReception의 progress를 "산불 외 종료"로 변경
			if ("no object".equals(detectedObject)) {
			    fireReception.setProgress("산불 외 종료");
			}
			// 데이터베이스에 저장
			fireReceptionRepository.save(fireReception);
			// 240303 수정
			
			// 데이터베이스에 저장
			fireSituationRoomRepository.save(fireSituationRoom);
			
			String fireStationFs = fireSituationRoom.getFireStation().getFs(); // FireStation의 FS 값을 가져옴
			int fireReceptionNum = fireSituationRoom.getFireReception().getNum(); // FireReception의 식별자(num) 값을 가져옴

			// ObjectMapper를 사용하여 필요한 정보만 포함한 JSON 문자열 생성
			String jsonMessage = objectMapper.writeValueAsString(Map.of(
				    "command", fireSituationRoom.getCommand(),
				    "state", fireSituationRoom.getState(),
				    "fireStationFs", fireStationFs, // FireStation의 FS
				    "fireReceptionNum", fireReceptionNum, // FireReception의 식별자		    
				    "address",fireReception.getAddress(),
				    "imgurl",fireReception.getImgurl()
				));

			// 리액트로 메시지 전송
			messagingTemplate.convertAndSend("/topic/firestation/" + fireStationFs, jsonMessage);
//			messagingTemplate.convertAndSend("/topic/messages", jsonMessage);

			System.out.println("FireSituationRoom 정보 전송 성공: " + jsonMessage);
			
			// result에 command 저장
			Result result = new Result();
			int commandValue = fireSituationRoom.getCommand();
			Optional<FireSituationRoom> fireSituationRoomOptional = fireSituationRoomRepository.findByCommand(commandValue);

			if (fireSituationRoomOptional.isPresent()) {
			    FireSituationRoom foundFireSituationRoom = fireSituationRoomOptional.get();
			    result.setFireSituationRoom(foundFireSituationRoom);
			    resultRepository.save(result);
			} else {
			    // FireSituationRoom 객체를 찾지 못한 경우의 처리
			    // 예를 들어, 새 FireSituationRoom 객체를 생성하거나, 오류 메시지를 반환할 수 있습니다.
				throw new RuntimeException("해당 command 값을 가진 FireSituationRoom 객체를 찾을 수 없습니다: " + commandValue);
			}
			

			System.out.println("Flask object detection results: " + detectedObject);
		} catch (JsonProcessingException e) {
			System.err.println("JSON 응답 파싱 중 오류 발생: " + e.getMessage());
		}
		
		System.out.println("메시지 수신 및 처리 성공: " + message);
		
		
	}
	
	// 사용자의 연락처 기준으로 본인이 신고한 게시물 보기
	public List<FireReceptionDto> findByHp(String hp) {
		return fireReceptionRepository.findByUser_HpOrderByAdateDesc(hp).stream().map(entity -> {
	        FireReceptionDto dto = new FireReceptionDto();
	        dto.setImgurl(entity.getImgurl());
	        dto.setGps(entity.getGps());
	        dto.setProgress(entity.getProgress());
	        dto.setAdate(entity.getAdate());
	        String address = getAddressIfNecessary(entity);
	        dto.setAddress(address);
	        return dto;
	    }).collect(Collectors.toList());
	}

	@Transactional
	private String getAddressIfNecessary(FireReception fireReception) {
	    if (fireReception.getAddress() == null || fireReception.getAddress().isEmpty()) {
	        String[] gpsCoordinates = fireReception.getGps().split(",");
	        double latitude = Double.parseDouble(gpsCoordinates[0]);
	        double longitude = Double.parseDouble(gpsCoordinates[1]);
	        String address = reverseGeocodingService.getAddress(latitude, longitude);
	        fireReception.setAddress(address);
	        fireReceptionRepository.saveAndFlush(fireReception);
	        return address;
	    }
	    return fireReception.getAddress();
	}
	
	// 산불 현황
    public long getCountByProgress(String progress) {
        return fireReceptionRepository.findAll().stream()
                .filter(n -> n.getProgress().equals(progress))
                .count();
    }
    
    public List<FireReceptionDto> getAllFireReceptions() {
        return fireReceptionRepository.findAll().stream().map(fireReception -> {
        	FireReceptionDto dto = new FireReceptionDto();
            dto.setNum(fireReception.getNum());
            dto.setImgurl(fireReception.getImgurl());
            dto.setAdate(fireReception.getAdate());
            dto.setGps(fireReception.getGps());
            dto.setProgress(fireReception.getProgress());
            return dto;
        }).collect(Collectors.toList());
    }
}
