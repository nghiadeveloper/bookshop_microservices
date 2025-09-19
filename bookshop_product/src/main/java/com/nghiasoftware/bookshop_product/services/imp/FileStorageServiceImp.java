package com.nghiasoftware.bookshop_product.services.imp;

import com.nghiasoftware.bookshop_product.exception.FileStorageException;
import com.nghiasoftware.bookshop_product.services.FileStorageServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServiceImp implements FileStorageServices {

    @Value("${file.path}")
    private String root;

    @Override
    public void save(MultipartFile file) {
        try {
            Path rootPath = Paths.get(root);
            if(!Files.exists(rootPath)){
                Files.createDirectory(rootPath);
            }
            Files.copy(file.getInputStream(), rootPath.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new FileStorageException("Failed to save file: " + file.getOriginalFilename() + " - " + e.getMessage());
        }
    }

    @Override
    public Resource load(String fileName) {
        return null;
    }

}
