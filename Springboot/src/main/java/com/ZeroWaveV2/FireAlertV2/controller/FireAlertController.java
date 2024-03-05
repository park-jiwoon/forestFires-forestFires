//package com.ZeroWaveV2.FireAlertV2.controller;
//
//import java.io.IOException;
//import java.util.Base64;
//import java.util.Date;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.ZeroWaveV2.FireAlertV2.dto.FireReceptionDto;
//import com.ZeroWaveV2.FireAlertV2.service.FireReceptionService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//
//@RestController
//@RequestMapping("/api")
//public class FireAlertController {
//	@Autowired
//	private KafkaTemplate<String, String> kafkaTemplate;
//
////	@Autowired
////	private FcmService fcmService; // FcmService 추가
//	
//	@Autowired
//	private FireReceptionService fireReceptionService;
//	
//	@PostMapping("/fireReception")
//    public ResponseEntity<String> sendFireAlert (
//            @RequestParam("image") MultipartFile image,
//            @RequestParam("gps") String gps,
//            @RequestParam("adate") String adateStr,
//            @AuthenticationPrincipal UserDetails currentUser) throws ParseException  {
//        try {
//            String hp = currentUser.getUsername();
//            // 이미지 처리
//            byte[] imageBytes = image.getBytes();
//            String imgurl = Base64.getEncoder().encodeToString(imageBytes);
//            
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//            Date adate = formatter.parse(adateStr);
//            
//            // FireReceptionDto 생성 및 설정
//            FireReceptionDto fireReceptionDto = new FireReceptionDto();
//            fireReceptionDto.setImgurl(imgurl); // 여기서는 예시로 Base64 인코딩된 이미지 URL을 설정합니다. 실제로는 파일을 저장하고 URL을 설정해야 합니다.
//            fireReceptionDto.setGps(gps);
//            fireReceptionDto.setAdate(adate); // adate를 적절한 형식으로 파싱해야 합니다.
//
//            // ObjectMapper를 사용하여 FireReceptionDto 객체를 JSON 문자열로 변환
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonMessage = objectMapper.writeValueAsString(fireReceptionDto);
//
//            // Kafka로 메시지 전송
//            kafkaTemplate.send("firealert_topic_v1", jsonMessage);
//            
//			// 여기에 FCM 알림 전송 로직 추가
////			String notificationResponse = fcmService.sendNotification("<TARGET_FCM_TOKEN>", "New Fire Alert",
////					"There is a new fire alert.");
////			System.out.println("Notification sent response: " + notificationResponse);
//
//
//            return ResponseEntity.ok("메시지 전송 성공");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메시지 전송 실패");
//        }
////		catch (Exception e) { // FcmService에서 발생할 수 있는 예외 처리
////		e.printStackTrace();
////		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FCM 알림 전송 실패");
////	}
//    }
//
//}
