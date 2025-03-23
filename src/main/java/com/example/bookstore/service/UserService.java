package com.example.bookstore.service;


import com.example.bookstore.dto.UserRegistrationDTO;
import com.example.bookstore.entity.User;
import com.example.bookstore.exception.ConflictException;
import com.example.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public User registerUser(UserRegistrationDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("Email already in use");
        }

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new ConflictException("Username already exists");
        }

        User user = User.builder()
            .email(dto.getEmail())
            .username(dto.getUsername())
            .password(passwordEncoder.encode(dto.getPassword()))
            .build();

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Email already in use");
        }
    }

}