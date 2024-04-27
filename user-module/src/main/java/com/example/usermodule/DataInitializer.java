package com.example.usermodule;


import com.example.usermodule.Domain.User;
import com.example.usermodule.Domain.UserRole;
import com.example.usermodule.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() {
        User user = User.builder()
                .email("user1@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .name("user1")
                .role(UserRole.valueOf("ROLE_USER"))
                .build();
        User user2 = User.builder()
                .email("user2@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .name("user2")
                .role(UserRole.valueOf("ROLE_USER"))
                .build();
        User user3 = User.builder()
                .email("user3@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .name("user3")
                .role(UserRole.valueOf("ROLE_USER"))
                .build();
        User user4 = User.builder()
                .email("user4@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .name("user4")
                .role(UserRole.valueOf("ROLE_USER"))
                .build();
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

    }
}