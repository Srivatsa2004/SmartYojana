package com.example.demo.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    // ✅ Add single user
    @PostMapping
    public User addUser(@RequestBody User user){
        System.out.println("User received: " + user);
        return userService.saveUser(user);
    }

    // ✅ Add multiple users (bulk)
    @PostMapping("/bulk")
    public List<User> addUsers(@RequestBody List<User> users){
        return userService.saveUsers(users);
    }

    // ✅ Get all users
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getUsers();
    }

    // ✅ Update user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user){
        return userService.updateUser(id, user);
    }

    // ✅ Delete user
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id){
        return userService.deleteUser(id);
    }
}