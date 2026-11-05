package com.example.demo.service;

import com.example.demo.model.Scheme;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {

    private final MLService mlService;
    private final SchemeService schemeService;

    public RecommendationService(MLService mlService, SchemeService schemeService) {
        this.mlService = mlService;
        this.schemeService = schemeService;
    }

    public List<Scheme> getRecommendedSchemes(User user) {
        List<Scheme> result = new ArrayList<>();

        if (user == null) {
            return result;
        }

        // 1. Try ML Service prediction first
        try {
            String predictedScheme = mlService.getPrediction(user);
            if (predictedScheme != null && !predictedScheme.trim().isEmpty()) {
                Scheme scheme = schemeService.findBySchemeName(predictedScheme);
                if (scheme != null) {
                    Double maxIncome = scheme.getMaxIncome();
                    if (maxIncome == null || maxIncome <= 0 || user.getIncome() <= maxIncome) {
                        result.add(scheme);
                        return result;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("ML recommendation fallback: " + e.getMessage());
        }

        // 2. Fallback to profile-based recommendation from SchemeService
        return schemeService.getRecommendedSchemes(user);
    }
}
