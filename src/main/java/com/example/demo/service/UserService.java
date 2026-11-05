package com.example.demo.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Save single user
    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            System.err.println("Could not save user to DB: " + e.getMessage());
            return user;
        }
    }

    // Save multiple users
    public List<User> saveUsers(List<User> users) {
        try {
            return userRepository.saveAll(users);
        } catch (Exception e) {
            System.err.println("Could not save users to DB: " + e.getMessage());
            return users;
        }
    }

    // Get all users
    public List<User> getUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            System.err.println("Could not get users from DB: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Update user
    public User updateUser(String id, User user) {
        user.setId(id);
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            System.err.println("Could not update user in DB: " + e.getMessage());
            return user;
        }
    }

    // Delete user
    public String deleteUser(String id) {
        try {
            userRepository.deleteById(id);
            return "User deleted successfully";
        } catch (Exception e) {
            return "Failed to delete user: " + e.getMessage();
        }
    }

    public User getUserById(String userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            return user.orElse(null);
        } catch (Exception e) {
            System.err.println("Could not find user: " + e.getMessage());
            return null;
        }
    }
}
