package com.masner.project2.dto.user;

import lombok.Data;

//devuelve
@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean active;

}
