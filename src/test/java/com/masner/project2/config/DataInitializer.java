package com.masner.project2.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.masner.project2.entity.User;
import com.masner.project2.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository) {
        return args -> {

            if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@admin.com");
                admin.setPassword("admin123"); // luego se encripta
                admin.setRole(User.Role.ADMIN);
                admin.setActive(true);

                userRepository.save(admin);

                System.out.println("✅ Admin creado automáticamente");
            }
        };
    }
}
