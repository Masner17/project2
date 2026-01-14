package com.masner.project2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masner.project2.entity.User;
import com.masner.project2.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUser(){
        return ResponseEntity.ok(userService.findAllActive());
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user){
        try{
            User newUser = userService.create(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user){
        User updateUser = userService.updateUser(user, id);
        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/desactive/{id}")
    public ResponseEntity<String> desactivateUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User desactivated succesfully");
    }
}