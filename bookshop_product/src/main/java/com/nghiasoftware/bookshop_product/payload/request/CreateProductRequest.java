package com.nghiasoftware.bookshop_product.payload.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateProductRequest {
    private String title;
    private String author;
    private int review;
    private double price;
    private MultipartFile[] files;
}
