package com.nghiasoftware.bookshop_product.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileRequest {
    private String fileName;
    private String file;
    private String checkSum;
}
