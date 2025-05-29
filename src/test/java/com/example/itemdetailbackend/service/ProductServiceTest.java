package com.example.itemdetailbackend.service;

import com.example.itemdetailbackend.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {

    private ObjectMapper objectMapper;

    private ProductService productService;

    private List<Product> testProducts;

    private Product testProduct;

    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        objectMapper = new ObjectMapper();

        testProducts = new ArrayList<>();
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setTitle("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(99.99);
        testProducts.add(testProduct);

        File testFile = tempDir.resolve("test-products.json").toFile();
        objectMapper.writeValue(testFile, testProducts);

        productService = new ProductService(objectMapper, testFile.getAbsolutePath());
        productService.init();
    }

    @Test
    void getProductById_WhenProductExists_ReturnsProduct() {
        Optional<Product> result = productService.getProductById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getTitle()).isEqualTo("Test Product");
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ReturnsEmpty() {
        Optional<Product> result = productService.getProductById(999);
        assertThat(result).isEmpty();
    }

    @Test
    void saveProduct_WhenNewProduct_SavesSuccessfully(@TempDir Path tempDir) throws IOException {
        File testFile = tempDir.resolve("test-products.json").toFile();
        objectMapper.writeValue(testFile, new ArrayList<>());

        ProductService localProductService = new ProductService(objectMapper, testFile.getAbsolutePath());
        localProductService.init();

        Product newProduct = new Product();
        newProduct.setTitle("New Product");

        localProductService.saveProduct(newProduct);

        List<Product> savedProducts = objectMapper.readValue(testFile, new TypeReference<>() {});
        assertThat(savedProducts).hasSize(1);
        assertThat(savedProducts.get(0).getTitle()).isEqualTo("New Product");
    }

    @Test
    void saveProduct_WhenDuplicateId_ThrowsException() {
        Product duplicateProduct = new Product();
        duplicateProduct.setId(1);

        assertThatThrownBy(() -> productService.saveProduct(duplicateProduct))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Product ID already exists");
    }
} 