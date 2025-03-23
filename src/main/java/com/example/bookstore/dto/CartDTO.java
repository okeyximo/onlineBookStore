package com.example.bookstore.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> cartItems;
}