package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Scheme;

import java.util.List;

public interface SchemeRepository extends MongoRepository<Scheme, String>{
    List<Scheme> findByOccupation(String occupation);

    List<Scheme> findByCategory(String category);

    List<Scheme> findByState(String state);

    List<Scheme> findByMaxIncomeLessThanEqual(double income);
    
}
