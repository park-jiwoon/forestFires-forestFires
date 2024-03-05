package com.ZeroWaveV2.FireAlertV2.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class FlaskCommunicationService {

    private final RestTemplate restTemplate;

    public FlaskCommunicationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendImageToFlaskAndReceiveResults(String imagePath) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(imagePath));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String flaskEndpoint = "http://localhost:5000/detect";
        ResponseEntity<String> response = restTemplate.postForEntity(flaskEndpoint, requestEntity, String.class);

        return response.getBody();
    }
}
