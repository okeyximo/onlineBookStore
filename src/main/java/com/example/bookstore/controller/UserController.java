package com.example.bookstore.controller;

import com.example.bookstore.dto.LoginRequestDTO;
import com.example.bookstore.dto.LoginResponseDTO;
import com.example.bookstore.dto.UserDTO;
import com.example.bookstore.dto.UserRegistrationDTO;
import com.example.bookstore.entity.User;
import com.example.bookstore.mapper.EntityMapper;
import com.example.bookstore.service.UserService;
import com.example.bookstore.config.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private  final EntityMapper entityMapper;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegistrationDTO dto) {
        User user = userService.registerUser(dto);
        return ResponseEntity.ok(entityMapper.toUserDTO(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        String token = jwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(LoginResponseDTO.builder().token(token).build());
    }
}