package com.example.demo.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.demo.model.Scheme;
import com.example.demo.model.User;
import com.example.demo.repository.SchemeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SchemeService {

    private final SchemeRepository schemeRepository;
    private final List<Scheme> bundledSchemes = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private volatile boolean mongoConnected = false;

    public SchemeService(SchemeRepository schemeRepository) {
        this.schemeRepository = schemeRepository;
    }

    @PostConstruct
    public void init() {
        loadBundledSchemes();
        trySyncWithMongo();
    }

    private void loadBundledSchemes() {
        try {
            ClassPathResource resource = new ClassPathResource("data/schemes.json");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    List<Scheme> loaded = objectMapper.readValue(is, new TypeReference<List<Scheme>>() {});
                    if (loaded != null && !loaded.isEmpty()) {
                        bundledSchemes.addAll(loaded);
                        System.out.println("Loaded " + bundledSchemes.size() + " bundled schemes from schemes.json");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Could not load bundled schemes.json: " + e.getMessage());
        }
    }

    private void trySyncWithMongo() {
        Thread thread = new Thread(() -> {
            try {
                long count = schemeRepository.count();
                mongoConnected = true;
                System.out.println("MongoDB connected successfully. Existing schemes count: " + count);
                if (count == 0 && !bundledSchemes.isEmpty()) {
                    System.out.println("MongoDB is empty. Auto-seeding " + bundledSchemes.size() + " schemes...");
                    schemeRepository.saveAll(bundledSchemes);
                    System.out.println("MongoDB auto-seeding completed.");
                }
            } catch (Exception e) {
                mongoConnected = false;
                System.out.println("MongoDB not connected (" + e.getMessage() + "). Operating with in-memory dataset.");
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public Scheme saveScheme(Scheme scheme) {
        bundledSchemes.add(scheme);
        if (mongoConnected) {
            try {
                return schemeRepository.save(scheme);
            } catch (Exception e) {
                mongoConnected = false;
            }
        }
        return scheme;
    }

    public List<Scheme> saveSchemes(List<Scheme> schemes) {
        bundledSchemes.addAll(schemes);
        if (mongoConnected) {
            try {
                return schemeRepository.saveAll(schemes);
            } catch (Exception e) {
                mongoConnected = false;
            }
        }
        return schemes;
    }

    public List<Scheme> getAllSchemes() {
        if (mongoConnected) {
            try {
                List<Scheme> list = schemeRepository.findAll();
                if (list != null && !list.isEmpty()) {
                    return list;
                }
            } catch (Exception e) {
                mongoConnected = false;
            }
        }
        return new ArrayList<>(bundledSchemes);
    }

    public String deleteScheme(String id) {
        bundledSchemes.removeIf(s -> id.equals(s.getId()));
        if (mongoConnected) {
            try {
                schemeRepository.deleteById(id);
            } catch (Exception e) {
                mongoConnected = false;
            }
        }
        return "Scheme deleted successfully";
    }

    public Scheme findBySchemeName(String schemeName) {
        if (schemeName == null || schemeName.trim().isEmpty()) {
            return null;
        }
        String cleanName = schemeName.trim();

        if (mongoConnected) {
            try {
                Optional<Scheme> opt = schemeRepository.findBySchemeName(cleanName);
                if (opt.isPresent()) {
                    return opt.get();
                }
            } catch (Exception e) {
                mongoConnected = false;
            }
        }

        String normalizedTarget = cleanName.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        for (Scheme s : bundledSchemes) {
            if (s.getSchemeName() != null) {
                String norm = s.getSchemeName().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                if (norm.equals(normalizedTarget) || norm.contains(normalizedTarget) || normalizedTarget.contains(norm)) {
                    return s;
                }
            }
        }
        return null;
    }

    public List<Scheme> getRecommendedSchemes(User user) {
        List<Scheme> allSchemes = getAllSchemes();
        List<Scheme> recommended = new ArrayList<>();

        if (user == null) {
            return recommended;
        }

        String uOcc = user.getOccupation() != null ? user.getOccupation().trim().toLowerCase() : "";
        String uCat = user.getCategory() != null ? user.getCategory().trim().toLowerCase() : "";
        String uState = user.getState() != null ? user.getState().trim().toLowerCase() : "";
        int uAge = user.getAge();
        double uIncome = user.getIncome();

        for (Scheme scheme : allSchemes) {
            // Age match
            boolean ageMatch = (scheme.getMinAge() <= 0 || uAge >= scheme.getMinAge()) &&
                               (scheme.getMaxAge() <= 0 || scheme.getMaxAge() >= 100 || uAge <= scheme.getMaxAge());

            // Income match
            boolean incomeMatch = scheme.getMaxIncome() == null ||
                                  scheme.getMaxIncome() <= 0.0 ||
                                  uIncome <= scheme.getMaxIncome();

            // Occupation match
            String sOcc = scheme.getOccupation() != null ? scheme.getOccupation().trim().toLowerCase() : "";
            boolean occMatch = sOcc.isEmpty() || sOcc.contains("all") || sOcc.contains("any") ||
                               (!uOcc.isEmpty() && sOcc.contains(uOcc));
            if (!occMatch && !uOcc.isEmpty()) {
                if (scheme.getEligibility() != null && scheme.getEligibility().toLowerCase().contains(uOcc)) {
                    occMatch = true;
                } else if (scheme.getDescription() != null && scheme.getDescription().toLowerCase().contains(uOcc)) {
                    occMatch = true;
                } else if (scheme.getSchemeName() != null && scheme.getSchemeName().toLowerCase().contains(uOcc)) {
                    occMatch = true;
                }
            }

            // Category match
            String sCat = scheme.getCategory() != null ? scheme.getCategory().trim().toLowerCase() : "";
            boolean catMatch = sCat.isEmpty() || sCat.contains("all") || sCat.contains("any") ||
                               (!uCat.isEmpty() && sCat.contains(uCat));
            if (!catMatch && !uCat.isEmpty()) {
                if (scheme.getEligibility() != null && scheme.getEligibility().toLowerCase().contains(uCat)) {
                    catMatch = true;
                } else if (scheme.getDescription() != null && scheme.getDescription().toLowerCase().contains(uCat)) {
                    catMatch = true;
                }
            }

            // State match
            String sState = scheme.getState() != null ? scheme.getState().trim().toLowerCase() : "";
            boolean stateMatch = sState.isEmpty() || sState.contains("all") || sState.contains("any") ||
                                 (!uState.isEmpty() && sState.contains(uState));

            if (ageMatch && incomeMatch && occMatch && catMatch && stateMatch) {
                recommended.add(scheme);
                if (recommended.size() >= 5) {
                    break;
                }
            }
        }

        // Fallback: match by occupation and category keyword if no exact match
        if (recommended.isEmpty() && !uOcc.isEmpty()) {
            for (Scheme scheme : allSchemes) {
                boolean matchesOcc = (scheme.getDescription() != null && scheme.getDescription().toLowerCase().contains(uOcc)) ||
                                     (scheme.getEligibility() != null && scheme.getEligibility().toLowerCase().contains(uOcc)) ||
                                     (scheme.getSchemeName() != null && scheme.getSchemeName().toLowerCase().contains(uOcc));
                if (matchesOcc) {
                    recommended.add(scheme);
                    if (recommended.size() >= 3) {
                        break;
                    }
                }
            }
        }

        return recommended;
    }
}
