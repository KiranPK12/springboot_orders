package com.quinbaytraining.orders.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private String message;
    private int status;
}
