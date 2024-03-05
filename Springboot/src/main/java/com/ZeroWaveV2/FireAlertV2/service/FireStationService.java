package com.ZeroWaveV2.FireAlertV2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.client.RestClientException;

import com.ZeroWaveV2.FireAlertV2.dto.FireSituationRoomDto;
import com.ZeroWaveV2.FireAlertV2.dto.FireStationDto;
import com.ZeroWaveV2.FireAlertV2.model.FireSituationRoom;
import com.ZeroWaveV2.FireAlertV2.model.FireStation;
import com.ZeroWaveV2.FireAlertV2.repository.FireStationRepository;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.IOException;
//import java.util.List;

@Service
public class FireStationService {

    private static final Logger logger = LoggerFactory.getLogger(FireStationService.class);

    private final FireStationRepository fireStationRepository;
    private final BCryptPasswordEncoder passwordEncoder;
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Value("${naver.client.id}")
//    private String naverClientId;
//
//    @Value("${naver.client.secret}")
//    private String naverClientSecret;

    @Autowired
    public FireStationService(FireStationRepository fireStationRepository, BCryptPasswordEncoder passwordEncoder) {
        this.fireStationRepository = fireStationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 로그인 기능은 유지됩니다.
    public FireStation fireStation_login(String fs, String password) {
        FireStation fireStation = fireStationRepository.findByFs(fs);
        if (fireStation != null && passwordEncoder.matches(password, fireStation.getPassword())) {
            return fireStation;
        }
        return null;
    }
    
    // 240228 수정
    // fs 값을 기반으로 FireStation 정보 조회
    public FireStation findFireStationByFs(String fs) {
        return fireStationRepository.findByFs(fs);
    }
    
    // 새로운 메소드: 주어진 gps 좌표에 가장 가까운 FireStation 찾기
    public FireStation findNearestFireStation(String gps) {
        List<FireStation> fireStations = fireStationRepository.findAll();
        String[] gpsParts = gps.split(",");
        double latitude = Double.parseDouble(gpsParts[0].trim());
        double longitude = Double.parseDouble(gpsParts[1].trim());

        FireStation nearestStation = null;
        double nearestDistance = Double.MAX_VALUE;

        for (FireStation station : fireStations) {
            double distance = calculateDistance(latitude, longitude, station.getLatitude(), station.getLongitude());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestStation = station;
            }
        }

        return nearestStation;
    }

    // Haversine 공식을 사용하여 두 위경도 좌표 사이의 거리를 계산
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구의 반경(km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // 거리(km)
    }
    
    // 소방서 가져오기
    public List<FireStationDto> findFireStationSummary(){
    	return fireStationRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }
    
    private FireStationDto convertEntityToDto(FireStation fireStation) {
    	FireStationDto fireStationDTO = new FireStationDto();
    	fireStationDTO.setFsName(fireStation.getFsName());
    	fireStationDTO.setFsAdd(fireStation.getFsAdd());
    	fireStationDTO.setFph(fireStation.getFph());
//    	fireStationDTO.setFs(fireStation.getFs());
        return fireStationDTO;
    }
    
    //
//    public List<FireStationDto> findFireStationsByFs() {
//    	return fireStationRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
//    }
    
    
    

    // Directions 5 API를 사용하여 가장 가까운 화재 관리소를 찾는 기능입니다.
//    public FireStation findClosestFireStation(String fireReceptionGps) {
//        String[] receptionCoords = fireReceptionGps.split(",");
//        double receptionLat = Double.parseDouble(receptionCoords[0]);
//        double receptionLon = Double.parseDouble(receptionCoords[1]);
//
//        FireStation closestStation = null;
//        double closestDistance = Double.MAX_VALUE;
//
//        List<FireStation> stations = fireStationRepository.findAll();
//        for (FireStation station : stations) {
//            // Directions 5 API를 호출합니다.
//            String url = buildDirectionsApiUrl(receptionLat, receptionLon, station.getLatitude(), station.getLongitude());
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("X-NCP-APIGW-API-KEY-ID", naverClientId);
//            headers.set("X-NCP-APIGW-API-KEY", naverClientSecret);
//
//            try {
//                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
//                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                    double distance = parseDistanceFromResponse(response.getBody());
//                    if (distance < closestDistance) {
//                        closestDistance = distance;
//                        closestStation = station;
//                    }
//                } else {
//                    logger.error("Failed to get a valid response from Naver Directions API. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
//                }
//            } catch (RestClientException e) {
//                logger.error("RestClientException while calling Naver Directions API: {}", e.getMessage());
//            }
//        }
//
//        if (closestStation == null) {
//            logger.warn("Could not find closest fire station for FireReception GPS: {}", fireReceptionGps);
//        } else {
//            logger.info("Closest fire station to FireReception GPS {} is: {}", fireReceptionGps, closestStation.getFs());
//        }
//
//        return closestStation;
//    }
//
//    private String buildDirectionsApiUrl(double startLat, double startLon, double goalLat, double goalLon) {
//        // 네이버 Directions 5 API URL 구성
//        // 실제 API 엔드포인트를 제공하는 URL이 필요합니다.
//        return String.format("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?start=%f,%f&goal=%f,%f", startLat, startLon, goalLat, goalLon);
//    }
//
//    private double parseDistanceFromResponse(String responseBody) {
//        try {
//            JsonNode jsonResponse = objectMapper.readTree(responseBody);
//            JsonNode route = jsonResponse.path("route");
//            if (!route.isMissingNode()) {
//                JsonNode traoptimal = route.path("traoptimal").get(0);
//                if (!traoptimal.isMissingNode()) {
//                    JsonNode summary = traoptimal.path("summary");
//                    if (!summary.isMissingNode()) {
//                        return summary.path("distance").asDouble();
//                    }
//                }
//            }
//            logger.error("Failed to parse the distance from the response body: {}", responseBody);
//        } catch (JsonProcessingException e) {
//            logger.error("JsonProcessingException occurred when parsing the response body: {}\nException: {}", responseBody, e.toString());
//        }
//        return Double.MAX_VALUE; // Return a large value to indicate failure
//    }

}
