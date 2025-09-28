package com.nghiasoftware.bookshop_product.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageServices {
    void save(MultipartFile file);
    Resource load(String fileName);
    void save(String fileName, byte[] file);
}
