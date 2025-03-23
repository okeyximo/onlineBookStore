package com.example.bookstore.controller;

import com.example.bookstore.config.UserDetailsImpl;
import com.example.bookstore.dto.CartDTO;
import com.example.bookstore.entity.User;
import com.example.bookstore.mapper.EntityMapper;
import com.example.bookstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final EntityMapper entityMapper;

    @GetMapping
    public CartDTO getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return entityMapper.toCartDTO(cartService.getCartByUser(user));
    }

    @PostMapping("/add")
    public CartDTO addToCart(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam Long bookId,
        @RequestParam int quantity) {
        User user = userDetails.getUser();
        return entityMapper.toCartDTO(cartService.addToCart(user, bookId, quantity));
    }

    @DeleteMapping("/remove")
    public CartDTO removeFromCart(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam Long bookId) {
        User user = userDetails.getUser();
        return entityMapper.toCartDTO(cartService.removeFromCart(user, bookId));
    }
}
