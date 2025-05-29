package com.example.itemdetailbackend.service;

import com.example.itemdetailbackend.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class ProductService {
    private List<Product> products;
    private final ObjectMapper objectMapper;
    private final String productsFilePath;

    public ProductService(ObjectMapper objectMapper, @Value("${data.products.file}") String productsFilePath) {
        this.objectMapper = objectMapper;
        this.productsFilePath = productsFilePath;
    }

    @PostConstruct
    public void init() throws IOException {
        File file = new File(productsFilePath);
        if (file.exists()) {
            products = objectMapper.readValue(file, new TypeReference<>() {});
        } else {
            products = new ArrayList<>();
        }
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
        saveToFile();
    }

    private void saveToFile() throws IOException {
        File file = new File(productsFilePath);
        if (!file.exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new IOException("Could not create directory: " + file.getParentFile().getAbsolutePath());
            }
            if (!file.createNewFile()) {
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