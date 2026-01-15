package com.masner.project2.mapper;

import com.masner.project2.dto.user.UserRequestDTO;
import com.masner.project2.dto.user.UserResponseDTO;
import com.masner.project2.entity.User;

public class UserMapper {
//DTO -> ENTITY

public static User toEntity(UserRequestDTO dto){
    User user = new User();
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    user.setPassword(dto.getPassword());
    return user;
}

//ENTITY -> DTO
public static UserResponseDTO toResponse(User user){
    UserResponseDTO dto = new UserResponseDTO();
    dto.setId(user.getId());
    dto.setName(user.getName());
    dto.setEmail(user.getEmail());
    dto.setRole(user.getRole().name());
    dto.setActive(user.isActive());

    return dto;
}
}
