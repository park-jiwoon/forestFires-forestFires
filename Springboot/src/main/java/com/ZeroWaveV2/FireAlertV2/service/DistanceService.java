//package com.ZeroWaveV2.FireAlertV2.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//import com.ZeroWaveV2.FireAlertV2.model.GeoPoint;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.json.JSONObject;
//import org.json.JSONArray;
//
//@Service
//public class DistanceService {
//
//    private final RestTemplate restTemplate;
//
//    @Value("${naver.client.id}")
//    private String naverClientId;
//
//    @Value("${naver.client.secret}")
//    private String naverClientSecret;
//
//    public DistanceService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public double getDistance(GeoPoint startPoint, GeoPoint endPoint) {
//        String url = UriComponentsBuilder.fromHttpUrl("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving")
//                .queryParam("start", startPoint.getLongitude() + "," + startPoint.getLatitude())
//                .queryParam("goal", endPoint.getLongitude() + "," + endPoint.getLatitude())
//                .toUriString();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("X-NCP-APIGW-API-KEY-ID", naverClientId);
//        headers.set("X-NCP-APIGW-API-KEY", naverClientSecret);
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//
//        System.out.println(response.getBody()); // API 응답을 로그로 출력
//
//        JSONObject jsonResponse = new JSONObject(response.getBody());
//
//        JSONObject route = jsonResponse.getJSONObject("route");
//
//        JSONArray traoptimal = route.getJSONArray("traoptimal");
//
//        // `traoptimal` 배열의 첫 번째 요소에서 `summary`를 가져옵니다.
//        JSONObject summary = traoptimal.getJSONObject(0).getJSONObject("summary");
//
//        // `summary`에서 `distance`를 가져옵니다.
//        double distance = summary.getDouble("distance");
//
//        return distance;
//    }
//}
