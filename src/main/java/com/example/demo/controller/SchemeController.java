package com.example.demo.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import com.example.demo.model.User;
import com.example.demo.model.Scheme;
import com.example.demo.service.RecommendationService;
import com.example.demo.service.SchemeService;
import com.example.demo.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/schemes")
public class SchemeController {

    private final SchemeService schemeService;
    private final RecommendationService recommendationService;
    private final UserService userService;

    public SchemeController(SchemeService schemeService,
                            RecommendationService recommendationService,
                            UserService userService) {
        this.schemeService = schemeService;
        this.recommendationService = recommendationService;
        this.userService = userService;
    }

    // Health check endpoint
    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "UP", "schemesCount", schemeService.getAllSchemes().size());
    }

    // Add single scheme
    @PostMapping
    public Scheme addScheme(@RequestBody Scheme scheme) {
        return schemeService.saveScheme(scheme);
    }

    // Add multiple schemes (bulk)
    @PostMapping("/bulk")
    public List<Scheme> addSchemes(@RequestBody List<Scheme> schemes) {
        return schemeService.saveSchemes(schemes);
    }

    // Get all schemes
    @GetMapping
    public List<Scheme> getAllSchemes() {
        return schemeService.getAllSchemes();
    }

    // Delete scheme
    @DeleteMapping("/{id}")
    public String deleteScheme(@PathVariable String id) {
        return schemeService.deleteScheme(id);
    }

    // Recommend schemes by user payload
    @PostMapping("/recommend")
    public List<Scheme> recommendSchemes(@RequestBody User user) {
        return recommendationService.getRecommendedSchemes(user);
    }

    // Recommend schemes by existing user id
    @GetMapping("/recommend/{userId}")
    public List<Scheme> recommend(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return Collections.emptyList();
        }
        return recommendationService.getRecommendedSchemes(user);
    }
}
