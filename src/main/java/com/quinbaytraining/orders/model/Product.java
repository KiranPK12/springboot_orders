package com.quinbaytraining.orders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int id;
    private String prodName;
    private double prodPrice;
    private long prodQuantity;


//    public Product(int id, String name) {
//        this.prodId = id;
//        this.prodName = name;
//    }

}
