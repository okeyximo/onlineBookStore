package com.example.bookstore.service;

import com.example.bookstore.entity.*;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.exception.UnauthorizedAccessException;
import com.example.bookstore.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Cart cart;
    private Book book;

    @BeforeEach
    public void setup() {
        user = User.builder().id(1L).username("testuser").build();
        book = Book.builder().id(1L).title("Test Book").price(BigDecimal.valueOf(20.00)).build();
        cart = Cart.builder()
            .user(user)
            .cartItems(List.of(
                CartItem.builder()
                    .book(book)
                    .quantity(2)
                    .priceAtTime(book.getPrice())
                    .build()
            ))
            .build();
    }

    @Test
    public void testCheckout_success() {
        when(cartService.getCartByUser(user)).thenReturn(cart);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.checkout(user, PaymentMethod.WEB);

        assertNotNull(order);
        assertEquals(user, order.getUser());
        assertEquals(1, order.getOrderItems().size()); // Single cart item
        assertEquals(BigDecimal.valueOf(40.00), order.getTotalPrice());
        verify(cartService, times(1)).clearCart(user);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testCheckout_emptyCart() {
        Cart emptyCart = Cart.builder().user(user).cartItems(Collections.emptyList()).build();
        when(cartService.getCartByUser(user)).thenReturn(emptyCart);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            orderService.checkout(user, PaymentMethod.WEB));
        assertEquals("Cannot checkout with an empty cart", exception.getMessage());
    }

    @Test
    public void testGetOrdersByUser() {
        List<Order> orders = List.of(Order.builder().id(1L).user(user).build());
        when(orderRepository.findByUserId(user.getId())).thenReturn(orders);

        List<Order> result = orderService.getOrdersByUser(user);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0).getUser());
    }

    @Test
    public void testGetOrderDetails_success() {
        Order order = Order.builder().id(1L).user(user).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderDetails(1L, user);
        assertEquals(order, result);
    }

    @Test
    public void testGetOrderDetails_notFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            orderService.getOrderDetails(1L, user));
        assertEquals("Order with ID 1 not found", exception.getMessage());
    }

    @Test
    public void testGetOrderDetails_unauthorized() {
        User otherUser = User.builder().id(2L).username("otheruser").build();
        Order order = Order.builder().id(1L).user(otherUser).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () ->
            orderService.getOrderDetails(1L, user));
        assertEquals("Order with ID 1 does not belong to user with ID 1", exception.getMessage());
    }
}