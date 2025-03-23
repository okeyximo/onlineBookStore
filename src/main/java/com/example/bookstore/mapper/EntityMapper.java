package com.example.bookstore.mapper;

import com.example.bookstore.dto.BookDTO;
import com.example.bookstore.dto.CartDTO;
import com.example.bookstore.dto.CartItemDTO;
import com.example.bookstore.dto.OrderDTO;
import com.example.bookstore.dto.OrderItemDTO;
import com.example.bookstore.dto.UserDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.CartItem;
import com.example.bookstore.entity.Order;
import com.example.bookstore.entity.OrderItem;
import com.example.bookstore.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public BookDTO toBookDTO(Book book) {
        return BookDTO.builder()
            .id(book.getId())
            .title(book.getTitle())
            .genre(book.getGenre())
            .isbn(book.getIsbn())
            .author(book.getAuthor())
            .yearOfPublication(book.getYearOfPublication())
            .price(book.getPrice())
            .build();
    }

    public Book toBook(BookDTO bookDTO) {
        return Book.builder()
            .id(bookDTO.getId())
            .title(bookDTO.getTitle())
            .genre(bookDTO.getGenre())
            .isbn(bookDTO.getIsbn())
            .author(bookDTO.getAuthor())
            .yearOfPublication(bookDTO.getYearOfPublication())
            .price(bookDTO.getPrice())
            .build();
    }

    public UserDTO toUserDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }

    public CartDTO toCartDTO(Cart cart) {
        return CartDTO.builder()
            .id(cart.getId())
            .userId(cart.getUser().getId())
            .cartItems(cart.getCartItems().stream().map(this::toCartItemDTO).collect(Collectors.toList()))
            .build();
    }

    public CartItemDTO toCartItemDTO(CartItem cartItem) {
        return CartItemDTO.builder()
            .id(cartItem.getId())
            .book(toBookDTO(cartItem.getBook()))
            .quantity(cartItem.getQuantity())
            .build();
    }

    public OrderDTO toOrderDTO(Order order) {
        return OrderDTO.builder()
            .id(order.getId())
            .userId(order.getUser().getId())
            .orderDate(order.getOrderDate())
            .paymentMethod(order.getPaymentMethod())
            .orderItems(order.getOrderItems().stream().map(this::toOrderItemDTO).collect(Collectors.toList()))
            .totalPrice(order.getTotalPrice())
            .build();
    }

    public OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
            .id(orderItem.getId())
            .book(toBookDTO(orderItem.getBook()))
            .quantity(orderItem.getQuantity())
            .build();
    }
}