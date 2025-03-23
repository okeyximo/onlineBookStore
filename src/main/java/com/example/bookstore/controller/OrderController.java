package com.example.bookstore.controller;


import com.example.bookstore.config.UserDetailsImpl;
import com.example.bookstore.dto.OrderDTO;
import com.example.bookstore.entity.PaymentMethod;
import com.example.bookstore.entity.User;
import com.example.bookstore.mapper.EntityMapper;
import com.example.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final EntityMapper entityMapper;

    @GetMapping
    public List<OrderDTO> getOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return orderService.getOrdersByUser(user)
                .stream()
                .map(entityMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderDetails(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        User user = userDetails.getUser();
        return entityMapper.toOrderDTO(orderService.getOrderDetails(id, user));
    }

    @PostMapping("/checkout")
    public OrderDTO checkout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam PaymentMethod paymentMethod) {
        User user = userDetails.getUser();
        return entityMapper.toOrderDTO(orderService.checkout(user, paymentMethod));
    }
}