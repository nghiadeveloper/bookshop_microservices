package com.nghiasoftware.bookshop_product.services.imp;

import com.nghiasoftware.bookshop_product.dto.ProductDTO;
import com.nghiasoftware.bookshop_product.entity.Product;
import com.nghiasoftware.bookshop_product.mapper.ProductMapper;
import com.nghiasoftware.bookshop_product.payload.request.CreateProductRequest;
import com.nghiasoftware.bookshop_product.payload.request.FileRequest;
import com.nghiasoftware.bookshop_product.payload.request.SearchProductRequest;
import com.nghiasoftware.bookshop_product.repository.ProductRepository;
import com.nghiasoftware.bookshop_product.services.FileStorageServices;
import com.nghiasoftware.bookshop_product.services.ProductServices;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ProductServiceImp implements ProductServices {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileStorageServices fileStorageServices;

    @Override
    public ProductDTO createProduct(CreateProductRequest productRequest) {
        StringBuilder images = new StringBuilder();
        List<FileRequest> files = productRequest.getFiles();

        if (files == null || files.isEmpty()) {
            // tùy yêu cầu: có thể cho phép không có file hoặc ném exception
        } else {
            for (FileRequest fr : files) {
                String fileName = fr.getFileName();
                String base64 = fr.getFile();
                String expectedChecksum = fr.getCheckSum();

                if (base64 == null) {
                    throw new IllegalArgumentException("File content is null for file: " + fileName);
                }

                byte[] fileBytes;
                try {
                    fileBytes = Base64.getDecoder().decode(base64);
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException("Invalid base64 for file: " + fileName, ex);
                }

                // tính SHA-256
                String checksum;
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(fileBytes);
                    checksum = Hex.encodeHexString(hash);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("SHA-256 algorithm not available", e);
                }

                if (!checksum.equalsIgnoreCase(expectedChecksum)) {
                    throw new RuntimeException("Checksum does not match for file: " + fileName);
                }
                fileStorageServices.save(fileName,fileBytes);
                images.append(fileName).append(",");
            }

            // xóa dấu phẩy cuối nếu có
            if (images.length() > 0 && images.charAt(images.length() - 1) == ',') {
                images.deleteCharAt(images.length() - 1);
            }
        }

        Product product = new Product();
        product.setTitle(productRequest.getTitle());
        product.setAuthor(productRequest.getAuthor());
        product.setReview(productRequest.getReview());
        product.setPrice(productRequest.getPrice());
        product.setImages(images.toString());

        product = productRepository.save(product);
        return ProductMapper.mapToDTO(product);
    }

    @Override
    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        return null;
    }

    @Override
    public void deleteProduct(String id) {

    }

    @Override
    public ProductDTO getProductById(String id) {
        return null;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::mapToDTO).toList();
    }

    @Override
    public Page<ProductDTO> searchProducts(SearchProductRequest request) {
        Specification<Product> specification = filter(request.getTitle(), request.getAuthor());
        return productRepository
                .findAll(specification, PageRequest.of(request.getNumPage(), request.getPageSize()))
                .map(ProductMapper::mapToDTO);
    }

    public Specification<Product> filter(String title, String author) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (author != null && !author.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%" + author.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
