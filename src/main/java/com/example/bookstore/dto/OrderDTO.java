package com.example.bookstore.dto;

import com.example.bookstore.entity.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class OrderDTO {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private PaymentMethod paymentMethod;
    private List<OrderItemDTO> orderItems;
    private BigDecimal totalPrice;
}