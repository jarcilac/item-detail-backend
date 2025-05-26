package com.example.itemdetailbackend.controller;

import com.example.itemdetailbackend.model.Product;
import com.example.itemdetailbackend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setTitle("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(99.99);
    }

    @Test
    void getProduct_WhenProductExists_ReturnsProduct() {
        when(productService.getProductById(1)).thenReturn(Optional.of(testProduct));

        ResponseEntity<Product> response = productController.getProduct(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1);
        assertThat(response.getBody().getTitle()).isEqualTo("Test Product");
    }

    @Test
    void getProduct_WhenProductDoesNotExist_ReturnsNotFound() {
        when(productService.getProductById(999)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProduct(999);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void createProduct_WhenValidProduct_ReturnsCreated() throws IOException {
        doNothing().when(productService).saveProduct(any(Product.class));

        ResponseEntity<Object> response = productController.createProduct(testProduct);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(testProduct);
        verify(productService).saveProduct(testProduct);
    }

    @Test
    void createProduct_WhenDuplicateId_ReturnsBadRequest() throws IOException {
        doThrow(new IllegalArgumentException("Product ID already exists")).when(productService).saveProduct(any(Product.class));

        ResponseEntity<Object> response = productController.createProduct(testProduct);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(java.util.Map.class);
        @SuppressWarnings("unchecked")
        String error = ((java.util.Map<String, String>) response.getBody()).get("error");
        assertThat(error).isEqualTo("Product ID already exists");
    }

    @Test
    void createProduct_WhenIOException_ReturnsInternalServerError() throws IOException {
        doThrow(new IOException()).when(productService).saveProduct(any(Product.class));

        ResponseEntity<Object> response = productController.createProduct(testProduct);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isInstanceOf(java.util.Map.class);
        @SuppressWarnings("unchecked")
        String error = ((java.util.Map<String, String>) response.getBody()).get("error");
        assertThat(error).isEqualTo("an exception occurred while saving the product");
    }
} 