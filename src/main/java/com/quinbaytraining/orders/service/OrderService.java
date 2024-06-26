package com.quinbaytraining.orders.service;

import com.quinbaytraining.orders.model.Order;
import com.quinbaytraining.orders.model.Product;

import java.util.List;

public interface OrderService {
    List<Product> getProductDetails();
    Order addOrder(Order order);
    Order getOrderById(String orderId);
    List<Order> getOrdersByCustomerId(String customerId);
}
