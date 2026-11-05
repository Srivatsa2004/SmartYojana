package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.demo.model.User;
import java.util.HashMap;
import java.util.Map;

@Service
public class MLService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ml.api.url:http://localhost:5001/predict}")
    private String mlApiUrl;

    public String getPrediction(User user) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("age", user.getAge());
            request.put("income", user.getIncome());
            request.put("occupation", user.getOccupation());
            request.put("category", user.getCategory());
            request.put("state", user.getState());

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(mlApiUrl, request, Map.class);

            if (response != null && response.getBody() != null) {
                Object scheme = response.getBody().get("scheme");
                if (scheme instanceof String) {
                    return (String) scheme;
                }
            }
        } catch (Exception e) {
            System.err.println("Notice: ML service call to " + mlApiUrl + " failed: " + e.getMessage());
        }
        return null;
    }
}
