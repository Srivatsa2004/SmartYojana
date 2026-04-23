package com.example.demo.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // ✅ Save single user
    public User saveUser(User user){
        return userRepository.save(user);
    }

    // ✅ Save multiple users
    public List<User> saveUsers(List<User> users){
        return userRepository.saveAll(users);
    }

    // ✅ Get all users
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    // ✅ Update user
    public User updateUser(String id, User user){
        user.setId(id);
        return userRepository.save(user);
    }

    // ✅ Delete user
    public String deleteUser(String id){
        userRepository.deleteById(id);
        return "User deleted successfully";
    }
     public User getUserById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);
    }
}