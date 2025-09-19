package com.nghiasoftware.bookshop_product.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchProductRequest {
    private String title;
    private String author;
    private int numPage;
    private int pageSize;
}
