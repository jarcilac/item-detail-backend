package com.example.itemdetailbackend.controller;

import java.util.Map;

import com.example.itemdetailbackend.model.Product;
import com.example.itemdetailbackend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController
{
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        return productService.getProductById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody @Valid Product product) {
        try {
            productService.saveProduct(product);
            return ResponseEntity.status(201).body(product);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(
                Map.of("error", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "an exception occurred while saving the product"));
        }
    }
} 