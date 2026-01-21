package com.masner.project2.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank
    private String email;

    @NotBlank 
    String password;

}
