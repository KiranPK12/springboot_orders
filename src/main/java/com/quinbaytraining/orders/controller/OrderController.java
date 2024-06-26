package com.quinbaytraining.orders.controller;

import com.quinbaytraining.orders.model.Order;
import com.quinbaytraining.orders.model.Product;
import com.quinbaytraining.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/products")
    public List<Product> getProductDetails() {
        return orderService.getProductDetails();
    }

    @PostMapping("/addOrder")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        return orderService.addOrder(order);
    }

    @GetMapping("/getOrderById/{orderId}")
    public ResponseEntity<?> getOrderProducts(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/getOrdersByCustomerId/{customerId}")
    public ResponseEntity<?> getOrdersByCustomerId(@PathVariable String customerId) {
        return orderService.getOrdersByCustomerId(customerId);
    }
}
