package com.quinbaytraining.orders.service.implementation;

import com.quinbaytraining.orders.model.Order;
import com.quinbaytraining.orders.model.Product;
import com.quinbaytraining.orders.repository.OrderRepository;
import com.quinbaytraining.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Product> getProductDetails() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<Product>> responseType = new ParameterizedTypeReference<List<Product>>() {};
        ResponseEntity<List<Product>> response = restTemplate.exchange("http://localhost:8080/product/getAll", HttpMethod.GET, entity, responseType);
        return response.getBody();
    }

    @Override
    public Order addOrder(Order order) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
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
                product.setProdQuantity(product.getProdQuantity() - productInOrder.getProdQuantity());
                HttpEntity<Product> requestEntity = new HttpEntity<>(product, headers);
                restTemplate.exchange("http://localhost:8080/product/updateProduct", HttpMethod.PUT, requestEntity, Void.class);
            }
        }
        if (order.getProducts().isEmpty()) {
            throw new RuntimeException("Insufficient product quantities for the order.");
        } else {
            return orderRepository.save(order);
        }
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order with ID " + orderId + " not found."));
    }

    @Override
    public List<Order> getOrdersByCustomerId(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}
