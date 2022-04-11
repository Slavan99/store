package com.example.store.service;

import com.example.store.entity.Product;
import com.example.store.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Autowired
    @InjectMocks
    private ProductService productService;

    private List<Product> productList;

    @BeforeEach
    private void prepareProductList() {
        Product product1 = new Product(1, "Sas", 20, 5.0);
        Product product2 = new Product(2, "Rar", 10, 10.0);
        productList = List.of(product1, product2);

    }

    @Test
    public void getProductByIdTest() {
        int id = 1;
        Mockito.lenient()
               .when(productRepository.findById(id))
               .thenReturn(java.util.Optional.of(productList.get(id - 1)));
        Product productById = productService.getProductById(id);
        assertEquals(productRepository.findById(id).get(), productById);
    }

    @Test
    public void getAllProductsTest() {
        Mockito.lenient()
               .when(productRepository.findAll())
               .thenReturn(productList);
        assertEquals(productRepository.findAll(), productService.getAllProducts());
    }

    @Test
    public void saveProductTest() {
        Product product = new Product(3, "To save", 5, 5.0);
        Mockito.lenient()
               .when(productRepository.save(product))
               .thenReturn(product);
        Product saveProduct = productService.saveProduct(product);
        assertEquals(product, saveProduct);
    }
}