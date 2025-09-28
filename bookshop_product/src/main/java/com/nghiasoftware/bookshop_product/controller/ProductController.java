package com.nghiasoftware.bookshop_product.controller;

import com.nghiasoftware.bookshop_product.payload.request.CreateProductRequest;
import com.nghiasoftware.bookshop_product.payload.request.SearchProductRequest;
import com.nghiasoftware.bookshop_product.payload.response.BaseResponse;
import com.nghiasoftware.bookshop_product.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductServices productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        BaseResponse response = new BaseResponse();
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setData(productService.getAllProducts());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(SearchProductRequest request) {
        BaseResponse response = new BaseResponse();
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Success");
        response.setData(productService.searchProducts(request).getContent());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest request) {
        BaseResponse response = new BaseResponse();
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Product created successfully");
        response.setData(productService.createProduct(request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
