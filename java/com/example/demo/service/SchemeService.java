package com.example.demo.service;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import com.example.demo.model.User;
import com.example.demo.model.Scheme;
import com.example.demo.repository.SchemeRepository;

@Service
public class SchemeService {

    private final SchemeRepository schemeRepository;

    public SchemeService(SchemeRepository schemeRepository) {
        this.schemeRepository = schemeRepository;
    }

 
    public Scheme saveScheme(Scheme scheme) {
        return schemeRepository.save(scheme);
    }

   
    public List<Scheme> saveSchemes(List<Scheme> schemes) {
        return schemeRepository.saveAll(schemes);
    }

    
    public List<Scheme> getAllSchemes() {
        return schemeRepository.findAll();
    }

    
    public String deleteScheme(String id) {
        schemeRepository.deleteById(id);
        return "Scheme deleted successfully";
    }

public List<Scheme> getRecommendedSchemes(User user) {

    List<Scheme> allSchemes = schemeRepository.findAll();
    List<Scheme> recommended = new ArrayList<>();

    for (Scheme scheme : allSchemes) {

        boolean ageMatch =
                user.getAge() >= scheme.getMinAge() &&
                user.getAge() <= scheme.getMaxAge();

        boolean incomeMatch =
                user.getIncome() <= scheme.getMaxIncome();

        boolean occupationMatch =
                scheme.getOccupation().equalsIgnoreCase("All") ||
                scheme.getOccupation().equalsIgnoreCase(user.getOccupation());

        boolean categoryMatch =
                scheme.getCategory().equalsIgnoreCase("All") ||
                scheme.getCategory().equalsIgnoreCase(user.getCategory());

        boolean stateMatch =
                scheme.getState().equalsIgnoreCase("All") ||
                scheme.getState().equalsIgnoreCase(user.getState());

        if (ageMatch && incomeMatch && occupationMatch && categoryMatch && stateMatch) {
            recommended.add(scheme);
        }
    }

    return recommended;
}
}

