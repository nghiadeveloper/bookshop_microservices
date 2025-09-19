package com.nghiasoftware.bookshop_product.exception;

import lombok.Getter;

@Getter
public class FileStorageException extends RuntimeException {

    private String message;

    public FileStorageException(String message) {
        super(message);
        this.message = message;
    }

}
