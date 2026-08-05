package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    @org.springframework.beans.factory.annotation.Value("${ml.api.url:http://localhost:5001/predict}")
    private String mlApiUrl;

    public String getPrediction(User user) {

       Map<String, Object> request = new HashMap<>();
        request.put("age", user.getAge());
        request.put("income", user.getIncome());
        request.put("occupation", user.getOccupation());
        request.put("category", user.getCategory());
        request.put("state", user.getState());

        ResponseEntity<Map> response =
                restTemplate.postForEntity(mlApiUrl, request, Map.class);

        return (String) response.getBody().get("scheme");
    }
}