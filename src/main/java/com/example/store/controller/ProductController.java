package com.example.store.controller;

import com.example.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(
            path = "/product",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object getAllProducts() {
        return productService.getAllProducts();

    }

}
