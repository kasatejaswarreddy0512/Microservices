package com.ktsr.controller;

import com.ktsr.DTO.ProductRequest;
import com.ktsr.DTO.ProductResponse;
import com.ktsr.model.Product;
import com.ktsr.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest){
        return new ResponseEntity<>(productService.createProduct(productRequest),HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProductResponse>> getALlProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }
}
