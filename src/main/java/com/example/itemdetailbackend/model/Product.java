package com.example.itemdetailbackend.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class Product {
    private Integer id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @Positive(message = "Price must be greater than 0")
    private double price;

    private List<@NotBlank(message = "Image URL cannot be blank") String> images;

    @PositiveOrZero(message = "Stock must be 0 or greater")
    private int stock;

    @NotBlank(message = "Condition is mandatory")
    private String condition;

    @NotNull(message = "Seller is mandatory")
    private Seller seller;

    @NotEmpty(message = "At least one payment method is required")
    private List<@NotNull(message = "Payment method cannot be null") PaymentMethod> paymentMethods;

    @NotNull(message = "Ratings are mandatory")
    private Ratings ratings;

    private List<Review> reviews;
}