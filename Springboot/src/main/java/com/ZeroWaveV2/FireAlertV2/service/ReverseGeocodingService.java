package com.ZeroWaveV2.FireAlertV2.service;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReverseGeocodingService {

   @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    private final String API_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr&coords=%s&sourcecrs=epsg:4326&orders=legalcode,admcode,addr,roadaddr&output=json";


    public String getAddress(double latitude, double longitude) {
        String coords = longitude + "," + latitude;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.add("X-NCP-APIGW-API-KEY", clientSecret);

        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<String> response = restTemplate.exchange(String.format(API_URL, coords), HttpMethod.GET, entity, String.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            String body = response.getBody();
            try {
                JSONObject jsonObject = new JSONObject(body);
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    if (result.getString("name").equals("roadaddr")) {
                       String address = result.getJSONObject("region").getJSONObject("area1").getString("name") + " " + 
                                result.getJSONObject("region").getJSONObject("area2").getString("name") + " " + 
                                result.getJSONObject("region").getJSONObject("area3").getString("name") + " " + 
                                result.getJSONObject("land").getString("name") + " " + 
                                result.getJSONObject("land").getString("number1");
                        return address;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}