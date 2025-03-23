package com.example.bookstore.dto;

import lombok.*;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
}