package com.masner.project2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.masner.project2.entity.User;
import com.masner.project2.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    //Obtener todos los usuarios
    public List<User> findAll(){
        return userRepository.findAll();
    }

    //Obtener usuario por id
    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    //Crear nuevo usuario
    public User create(User user){
        validate(user);
        Optional<User> exist = userRepository.findByEmail(user.getEmail());
        if (!exist.isPresent()) {
            throw new IllegalArgumentException("There is already a user with that email address.");
        }

        if (user.getRole()==null){
            user.setRole(User.Role.CUSTOMER); //aca al poner automatico rol customer para los nuevos clientes que se registe me sale un error tambien
        }

        return userRepository.save(user);
    }

    private void validate(User user){
		if (user.getName() == null || user.getName().isBlank()) {
			throw new IllegalArgumentException("The name is required");
		}
		if (user.getEmail() == null || user.getEmail().isBlank()) {
			throw new IllegalArgumentException("The email is required");
		}
		if (user.getPassword() == null || user.getPassword().isBlank()) {
			throw new IllegalArgumentException("The password is required");
		}        
    }

    public User updateUser (User user, Long id){
        User userValidate = userRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("User does not exist"));
        
            validate(user);
        
            Optional<User> exist = userRepository.findByEmail(user.getEmail());
        if (exist.isPresent() && !exist.get().getId().equals(id)) {
            throw new IllegalArgumentException("There is already a user with that email address.");
            }

        userValidate.setEmail(user.getEmail());
        userValidate.setName(user.getName());
        userValidate.setPassword(user.getPassword());
        
            return userRepository.save(userValidate);
    }

        //Borrar
    public void deleteAsset(Long id){
        User userExist = userRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("user does not exist"));
        
        userRepository.deleteById(userExist.getId());
        }
}
