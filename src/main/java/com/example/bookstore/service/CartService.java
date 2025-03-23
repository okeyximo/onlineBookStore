package com.example.bookstore.service;


import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.CartItem;
import com.example.bookstore.entity.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final BookRepository bookRepository;

    public Cart getCartByUser(User user) {

        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = Cart.builder().user(user).cartItems(new ArrayList<>()).build();
                    return cartRepository.save(cart);
                });
    }

    public Cart addToCart(User user, Long bookId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        Cart cart = getCartByUser(user);
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        Optional<CartItem> existingItem = cart.getCartItems().stream()
            .filter(item -> item.getBook().getId().equals(bookId))
            .findFirst();
        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = CartItem.builder()
                .book(book)
                .quantity(quantity)
                .priceAtTime(book.getPrice())
                .cart(cart)
                .build();
            cart.getCartItems().add(newItem);
        }
        return cartRepository.save(cart);
    }

    public Cart removeFromCart(User user, Long bookId) {
        Cart cart = getCartByUser(user);
        cart.getCartItems().removeIf(item -> item.getBook().getId().equals(bookId));
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(User user) {
        Cart cart = getCartByUser(user);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}