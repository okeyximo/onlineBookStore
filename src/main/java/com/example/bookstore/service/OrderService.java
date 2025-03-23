package com.example.bookstore.service;


import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.Order;
import com.example.bookstore.entity.OrderItem;
import com.example.bookstore.entity.PaymentMethod;
import com.example.bookstore.entity.User;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.exception.UnauthorizedAccessException;
import com.example.bookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartService cartService;


    public Order checkout(User user, PaymentMethod paymentMethod) {
        Cart cart = cartService.getCartByUser(user);
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout with an empty cart");
        }

        BigDecimal totalPrice = calculateTotal(cart);

        boolean paymentSuccessful = simulatePayment(paymentMethod, totalPrice);
        if (!paymentSuccessful) {
            throw new RuntimeException("Payment failed for method: " + paymentMethod);
        }

        Order order = Order.builder()
            .user(user)
            .orderDate(LocalDate.now())
            .paymentMethod(paymentMethod)
            .totalPrice(totalPrice)
            .build();

        List<OrderItem> orderItems = getOrderItems(cart, order);

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(user);
        return savedOrder;
    }

    private List<OrderItem> getOrderItems(Cart cart, Order order) {
        return cart.getCartItems().stream()
            .map(item -> OrderItem.builder()
                .book(item.getBook())
                .quantity(item.getQuantity())
                .priceAtTime(item.getPriceAtTime())
                .order(order)
                .build())
            .collect(Collectors.toList());
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserId(user.getId());
    }

    public Order getOrderDetails(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order with ID " + orderId + " not found"));
        if (order.getUser() == null || !order.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Order with ID " + orderId + " does not belong to user with ID " + user.getId());
        }
        return order;
    }

    private boolean simulatePayment(PaymentMethod paymentMethod, BigDecimal total) {
        log.info("Processing payment of {} via {}...", total, paymentMethod);
        return true;
    }
    private BigDecimal calculateTotal(Cart cart) {
        return cart.getCartItems().stream()
            .map(item -> item.getPriceAtTime().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}