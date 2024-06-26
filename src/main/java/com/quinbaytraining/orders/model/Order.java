package com.quinbaytraining.orders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "orders")
@AllArgsConstructor
@NoArgsConstructor
//TODO:Remove totalPrice and totalQuantity
public class Order {
    @Id
    private String id;
    private String customerName;
    private String customerId;
    private List<Product> products;
}
