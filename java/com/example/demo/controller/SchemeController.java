package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.User;
import com.example.demo.model.Scheme;
import com.example.demo.service.RecommendationService;
import com.example.demo.service.SchemeService;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@CrossOrigin(origins = "http://localhost:3000") 
@RestController
@RequestMapping("/schemes")
public class SchemeController {

    private final SchemeService schemeService;

    public SchemeController(SchemeService schemeService) {
        this.schemeService = schemeService;
    }

    // ✅ Add single scheme
    @PostMapping
    public Scheme addScheme(@RequestBody Scheme scheme) {
        return schemeService.saveScheme(scheme);
    }

    // ✅ Add multiple schemes (bulk)
    @PostMapping("/bulk")
    public List<Scheme> addSchemes(@RequestBody List<Scheme> schemes) {
        return schemeService.saveSchemes(schemes);
    }

    // ✅ Get all schemes
    @GetMapping
    public List<Scheme> getAllSchemes() {
        return schemeService.getAllSchemes();
    }

    // ✅ Delete scheme
    @DeleteMapping("/{id}")
    public String deleteScheme(@PathVariable String id) {
        return schemeService.deleteScheme(id);
    }
    @PostMapping("/recommend")
    public List<Scheme> recommendSchemes(@RequestBody User user) {
        return schemeService.getRecommendedSchemes(user);
    }
    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserService userService;

    @GetMapping("/recommend/{userId}")
    public List<Scheme> recommend(@PathVariable String userId) {

    User user = userService.getUserById(userId);
    

    return recommendationService.getRecommendedSchemes(user);
}
}