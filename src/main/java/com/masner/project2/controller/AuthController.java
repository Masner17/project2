package com.masner.project2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masner.project2.dto.auth.LoginResponseDTO;
import com.masner.project2.security.JwtService;
import com.masner.project2.dto.auth.LoginRequestDTO;
import com.masner.project2.service.UserService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService){
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request){

        LoginResponseDTO response = userService.login(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(response);
            }
}
