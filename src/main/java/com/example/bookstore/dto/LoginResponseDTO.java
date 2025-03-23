package com.example.bookstore.dto;

import lombok.*;

@Data
@Builder
public class LoginResponseDTO {
    private String token;
}