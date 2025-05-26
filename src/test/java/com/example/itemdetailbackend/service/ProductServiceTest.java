package com.example.itemdetailbackend.service;

import com.example.itemdetailbackend.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ClassPathResource resource;

    @InjectMocks
    private ProductService productService;

    private List<Product> testProducts;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProducts = new ArrayList<>();
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setTitle("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(99.99);
        testProducts.add(testProduct);
    }

    @Test
    void init_LoadsProductsFromJson() throws IOException {
        // Arrange
        String json = "[{\"id\":1,\"title\":\"Test Product\"}]";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes());
        
        when(resource.getInputStream()).thenReturn(inputStream);
        when(objectMapper.readValue(any(ByteArrayInputStream.class), any(TypeReference.class)))
            .thenReturn(testProducts);

        // Act
        productService.init();

        // Assert
        Optional<Product> result = productService.getProductById(1);
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
    }

    @Test
    void getProductById_WhenProductExists_ReturnsProduct() throws IOException {
        // Arrange
        productService.init(); // Aseguramos que los productos estén cargados
        
        // Act
        Optional<Product> result = productService.getProductById(1);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getTitle()).isEqualTo("Test Product");
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ReturnsEmpty() throws IOException {
        // Arrange
        productService.init(); // Aseguramos que los productos estén cargados
        
        // Act
        Optional<Product> result = productService.getProductById(999);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void saveProduct_WhenNewProduct_SavesSuccessfully() throws IOException {
        // Arrange
        Product newProduct = new Product();
        newProduct.setTitle("New Product");
        File mockFile = mock(File.class);
        
        when(resource.getFile()).thenReturn(mockFile);
        when(mockFile.exists()).thenReturn(true);
        doNothing().when(objectMapper).writeValue(any(File.class), any(List.class));

        // Act
        productService.saveProduct(newProduct);

        // Assert
        verify(objectMapper).writeValue(any(File.class), any(List.class));
    }

    @Test
    void saveProduct_WhenDuplicateId_ThrowsException() throws IOException {
        // Arrange
        productService.init(); // Carga los productos existentes
        Product duplicateProduct = new Product();
        duplicateProduct.setId(1); // ID que ya existe

        // Act & Assert
        assertThatThrownBy(() -> productService.saveProduct(duplicateProduct))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Product ID already exists");
    }

    @Test
    void saveProduct_WhenFileDoesNotExist_CreatesFile() throws IOException {
        // Arrange
        Product newProduct = new Product();
        newProduct.setTitle("New Product");
        File mockFile = mock(File.class);
        File mockParentFile = mock(File.class);
        
        when(resource.getFile()).thenReturn(mockFile);
        when(mockFile.exists()).thenReturn(false);
        when(mockFile.getParentFile()).thenReturn(mockParentFile);
        when(mockParentFile.mkdirs()).thenReturn(true);
        when(mockFile.createNewFile()).thenReturn(true);

        // Act
        productService.saveProduct(newProduct);

        // Assert
        verify(mockFile).createNewFile();
        verify(objectMapper).writeValue(eq(mockFile), any(List.class));
    }
} 