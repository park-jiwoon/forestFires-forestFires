package com.ZeroWaveV2.FireAlertV2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeocodingService {

    private static final Logger logger = LoggerFactory.getLogger(GeocodingService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

    public double[] getGeoLocationFromAddress(String address) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", naverClientId);
        headers.set("X-NCP-APIGW-API-KEY", naverClientSecret);

        String formattedUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=%s";
        String url = String.format(formattedUrl, address);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                JsonNode addresses = jsonResponse.get("addresses");

                if (addresses != null && !addresses.isEmpty()) {
                    JsonNode addressNode = addresses.get(0);
                    double latitude = addressNode.get("y").asDouble();
                    double longitude = addressNode.get("x").asDouble();
                    return new double[]{latitude, longitude};
                } else {
                    logger.error("No geocoding result found for address: {}", address);
                }
            } else {
                logger.error("Non-OK response from geocoding API for address: {}, status code: {}", address, response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.error("RestClientException when calling geocoding API for address: {}", address, e);
        } catch (Exception e) {
            logger.error("Exception when parsing the geocoding API response for address: {}", address, e);
        }
        return null; // Return null in case of any failure
    }
}

