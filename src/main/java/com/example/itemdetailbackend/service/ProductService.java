package com.example.itemdetailbackend.service;

import com.example.itemdetailbackend.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class ProductService {
    private List<Product> products;
    private final ObjectMapper objectMapper;

    public ProductService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("data/products.json");
        products = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() { });
    }

    public Optional<Product> getProductById(int id) {
        return products.stream()
                .filter(product -> product.getId() == id)
                .findFirst();
    }

    public void saveProduct(Product product) throws IOException {
        if (isNull(product.getId())) {
            product.setId(calculateNextProductId());
        }

        boolean idExists = products.stream()
            .anyMatch(existingProduct -> existingProduct.getId().equals(product.getId()));

        if (idExists) {
            throw new IllegalArgumentException("Product ID already exists: " + product.getId());
        }

        products.add(product);
        ClassPathResource resource = new ClassPathResource("data/products.json");
        File file = resource.getFile();

        if (!file.exists()) {
            if (!file.getParentFile().mkdirs() && !file.createNewFile()) {
                throw new IOException("Could not create file: " + file.getAbsolutePath());
            }
        }

        objectMapper.writeValue(file, products);
    }

    private int calculateNextProductId() {
        return products.stream()
            .mapToInt(Product::getId)
            .max()
            .orElse(0) + 1;
    }
} 