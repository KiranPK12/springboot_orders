package com.quinbaytraining.orders.service;

import com.quinbaytraining.orders.model.Order;
import com.quinbaytraining.orders.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RestTemplate restTemplate;

    public List<Product> getProductDetails() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.put("Authorization", new ArrayList<>());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<Product>> responseType = new ParameterizedTypeReference<List<Product>>() {
        };
        return restTemplate.exchange("http://localhost:8080/product/getAll", HttpMethod.GET, entity, responseType).getBody();
    }

    public ResponseEntity<Order> addOrder(Order order) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.put("Authorization", new ArrayList<>());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        Iterator<Product> iterator = order.getProducts().iterator();
        while (iterator.hasNext()) {
            Product productInOrder = iterator.next();
            ResponseEntity<Product> response = restTemplate.exchange(
                    "http://localhost:8080/product/getProduct/" + productInOrder.getId(),
                    HttpMethod.GET,
                    entity,
                    Product.class);
            Product product = response.getBody();
            if (product == null || product.getProdQuantity() < productInOrder.getProdQuantity()) {
                iterator.remove();
            } else {
                String url = "http://localhost:8080/product/updateProduct/" + productInOrder.getId();
                product.setProdQuantity(product.getProdQuantity() - productInOrder.getProdQuantity());
                HttpEntity<Product> requestEntity = new HttpEntity<>(product, headers);
                restTemplate.put(url, requestEntity);
            }
        }
        if (order.getProducts().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            mongoTemplate.save(order);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        }
    }

    public ResponseEntity<?> getOrderById(String orderId) {
        Order order = mongoTemplate.findById(orderId, Order.class);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            String message = "Order with ID " + orderId + " not found.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    public ResponseEntity<?> getOrdersByCustomerId(String customerId) {
        List<Order> orders = mongoTemplate.find(
                Query.query(Criteria.where("customerId").is(customerId)),
                Order.class
        );
        if (!orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            String message = "No orders found for customer ID: " + customerId;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }
}
