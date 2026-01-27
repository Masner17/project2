package com.masner.project2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.masner.project2.dto.auth.LoginResponseDTO;
import com.masner.project2.entity.User;
import com.masner.project2.repository.UserRepository;
import com.masner.project2.security.JwtService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    //Obtener todos los usuarios
    public List<User> findAll(){
        return userRepository.findAll();
    }
        //Obtener todos los user disponibles
    public List<User> findAllActive() {
        return userRepository.findByActiveTrue();
    }

    //Obtener usuario por id
    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    //Crear nuevo usuario
    public User create(User user){
        validate(user);
        Optional<User> exist = userRepository.findByEmail(user.getEmail());
        if (exist.isPresent()) {
            throw new IllegalArgumentException("There is already a user with that email address.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole()==null){
            user.setRole(User.Role.CUSTOMER); 
        }
        user.setActive(true);
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
        userValidate.setActive(user.isActive());
        
            return userRepository.save(userValidate);
    }

        //Borrar
    public void deleteUser(Long id){
        User userExist = userRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("user does not exist"));
        
        userExist.setActive(false);
        userRepository.save(userExist);
        }

    public LoginResponseDTO login (String email, String rawPassword){
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!user.isActive()){
            throw new IllegalArgumentException("User is inactive");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())){
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail() , user.getRole().name());

        return new LoginResponseDTO(token);
    }
}
