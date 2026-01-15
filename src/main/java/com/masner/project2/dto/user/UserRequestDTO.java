package com.masner.project2.dto.user;

import lombok.Data;

//Para crear y actualizar
@Data
public class UserRequestDTO {

    private String name;
    private String email;
    private String password;

    //Solo lo que el cliente puede modificar
}
