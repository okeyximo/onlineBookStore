package com.example.bookstore.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Builder
public class CartItemDTO {
    private Long id;
    private BookDTO book;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}