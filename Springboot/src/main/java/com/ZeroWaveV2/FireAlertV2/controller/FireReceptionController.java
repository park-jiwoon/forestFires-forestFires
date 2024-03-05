package com.ZeroWaveV2.FireAlertV2.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ZeroWaveV2.FireAlertV2.dto.FireReceptionDto;
import com.ZeroWaveV2.FireAlertV2.model.FireReception;
import com.ZeroWaveV2.FireAlertV2.model.Guest;
import com.ZeroWaveV2.FireAlertV2.model.Member;
import com.ZeroWaveV2.FireAlertV2.repository.FireReceptionRepository;
import com.ZeroWaveV2.FireAlertV2.repository.GuestRepository;
// 240228 수정
import com.ZeroWaveV2.FireAlertV2.repository.MemberRepository;
import com.ZeroWaveV2.FireAlertV2.service.FireReceptionService;
import com.google.api.client.util.Value;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/fireReception")
public class FireReceptionController {
    
    @Autowired
    private FireReceptionService fireReceptionService;
    
    @Autowired
	private FireReceptionRepository fireReceptionRepository;

	@Value("${firealert.image.storage}")
	private String imageStoragePath;

	// 240228 수정
	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private GuestRepository guestRepository;
	
    @GetMapping
    public ResponseEntity<List<FireReceptionDto>> getFireReceptions(@AuthenticationPrincipal UserDetails currentUser) {
        // UserDetails에서 사용자의 hp를 추출
        String hp = currentUser.getUsername(); // JWT 토큰에 저장된 hp 정보 사용

        // 해당 hp를 기반으로 FireReception 정보 조회
        List<FireReceptionDto> fireReceptions = fireReceptionService.findByHp(hp);
        
        if (fireReceptions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(fireReceptions);
    }

    @PostMapping
    public ResponseEntity<Void> receiveFireAlert(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestParam("image") MultipartFile image,
                                                 @RequestParam("gps") String gps) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userHp = userDetails.getUsername();
            fireReceptionService.processFireAlert(userHp, image, gps);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }
    
    // 산불 현황
    @GetMapping("/count/{progressCode}")
    public ResponseEntity<Long> getCountByProgressCode(@PathVariable int progressCode) {
        String progress;
        switch (progressCode) {
            case 0:
                progress = "진화 중";
                break;
            case 1:
                progress = "진화 완료";
                break;
            case 2:
                progress = "산불 외 종료";
                break;
            default:
                return ResponseEntity.badRequest().body(null);
        }
        long count = fireReceptionService.getCountByProgress(progress);
        return ResponseEntity.ok(count);
    }
    
    // 특정 사용자의 FireReception 데이터 조회
 	@GetMapping("/user")
 	public ResponseEntity<List<FireReceptionDto>> getFireReceptionsForUser(
 			@AuthenticationPrincipal UserDetails currentUser) {
 		String hp = currentUser.getUsername();
 		List<FireReception> fireReceptions = fireReceptionRepository.findByUser_Hp(hp);
 		if (fireReceptions.isEmpty()) {
 			return ResponseEntity.notFound().build();
 		}

 		List<FireReceptionDto> dtos = fireReceptions.stream()
 	            .map(reception -> new FireReceptionDto(reception.getNum(), reception.getUser().getHp(),
 	                    reception.getImgurl(), reception.getAdate(), reception.getGps(), reception.getProgress(),
 	                    reception.getFireSituationRoom())) // fireSituationRoom 필드 추가
 	            .collect(Collectors.toList());

 		return ResponseEntity.ok(dtos);
 	}

 	// 모든 FireReception 데이터 조회
 	@GetMapping("/all")
 	public ResponseEntity<List<FireReceptionDto>> getAllFireReceptions() {
 	    List<FireReception> fireReceptions = fireReceptionRepository.findAll();
 	    List<FireReceptionDto> dtos = fireReceptions.stream()
 	            .map(reception -> new FireReceptionDto(reception.getNum(), reception.getUser().getHp(),
 	                    reception.getImgurl(), reception.getAdate(), reception.getGps(), reception.getProgress(),
 	                    reception.getFireSituationRoom())) // fireSituationRoom 필드 추가
 	            .collect(Collectors.toList());

 	    return ResponseEntity.ok(dtos);
 	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file, @RequestParam("gps") String gps,
	                                     @AuthenticationPrincipal UserDetails currentUser) throws IOException {
	    String userHp = currentUser.getUsername();
	    // 240228 수정
	    Member user = memberRepository.findByHp(userHp);
	    Guest guest = null;
	    if (user == null) {
	        guest = guestRepository.findByHp(userHp);
	    }
	    if(user == null && guest == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found, " + userHp);
	    }
	
	    String filename = file.getOriginalFilename();
	    Path directoryPath = Paths.get(imageStoragePath);
	    if (!Files.exists(directoryPath)) {
	        Files.createDirectories(directoryPath);
	    }
	
	    Path filePath = directoryPath.resolve(filename);
	    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	
	    FireReception fireReception = new FireReception();
	    fireReception.setImgurl(filename);
	    fireReception.setAdate(new Date());
	    fireReception.setGps(gps);
	    
	    // Assign the user or guest to the FireReception entity appropriately
	    if (user != null) {
	        fireReception.setUser(user); // Assuming FireReception has a setUser method to link to Member
	    } else if (guest != null) {
	        fireReception.setUser(guest); // Assuming FireReception has a setGuest method to link to Guest
	    }
	
	    fireReceptionRepository.save(fireReception);
	
	    return ResponseEntity.ok().body("Image uploaded successfully");
	}

	@GetMapping("/images/{imageName:.+}")
	public ResponseEntity<byte[]> getImageAsResponseEntity(@PathVariable String imageName) {
		try {
			Path imagePath = Paths.get(imageStoragePath, imageName);
			byte[] image = Files.readAllBytes(imagePath);
	
			// 파일의 MIME 타입을 동적으로 확인
			String contentType = Files.probeContentType(imagePath);
			if (contentType == null) {
				// 기본값 설정: 파일 타입을 결정할 수 없는 경우 application/octet-stream 으로 설정
				contentType = "application/octet-stream";
			}
			MediaType mediaType = MediaType.parseMediaType(contentType);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(mediaType);
	
			return new ResponseEntity<>(image, headers, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}	
	}

	// 관리자용 전체 접수현황 보기
	@GetMapping("/admin")
    public List<FireReceptionDto> getAllFireReception() {
        return fireReceptionService.getAllFireReceptions();
    }
}
