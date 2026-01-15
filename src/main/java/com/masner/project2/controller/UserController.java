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

import com.masner.project2.dto.user.UserRequestDTO;
import com.masner.project2.dto.user.UserResponseDTO;
import com.masner.project2.entity.User;
import com.masner.project2.mapper.UserMapper;
import com.masner.project2.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> users = userService.findAll()
            .stream()
            .map(UserMapper::toResponse)
            .toList();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserResponseDTO>> getActiveUser(){
        List<UserResponseDTO> users = userService.findAllActive()
            .stream()
            .map(UserMapper::toResponse)
            .toList();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody UserRequestDTO request){
        try{
            User user = UserMapper.toEntity(request);
            User saved = userService.create(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserMapper.toResponse(saved));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO request){
        User user = UserMapper.toEntity(request);
        User updated = userService.updateUser(user, id);
        return ResponseEntity.ok(UserMapper.toResponse(updated));
    }

    @PutMapping("/desactive/{id}")
    public ResponseEntity<String> desactivateUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User desactivated succesfully");
    }
}

//ResponseEntity permite controlar http status, body y headers