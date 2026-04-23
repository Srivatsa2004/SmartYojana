package com.example.demo.service;

import com.example.demo.model.Scheme;
import com.example.demo.model.User;
import com.example.demo.service.MLService;
import com.example.demo.repository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    @Autowired
    private MLService mlService;

    @Autowired
    private SchemeRepository schemeRepository;

    public List<Scheme> getRecommendedSchemes(User user) {

        String predictedScheme = mlService.getPrediction(user);
        

        List<Scheme> result = new ArrayList<>();

        Optional<Scheme> schemeOpt = schemeRepository.findById(predictedScheme);

        if (schemeOpt.isPresent()) {
            Scheme scheme = schemeOpt.get();

            // Optional rule check
            Double maxIncome = scheme.getMaxIncome();

            if (maxIncome == null || user.getIncome() <= maxIncome) {
                result.add(scheme);
            }
        } 
        return result;
    }
}