package com.nghiasoftware.bookshop_product.services.imp;

import com.nghiasoftware.bookshop_product.dto.ProductDTO;
import com.nghiasoftware.bookshop_product.entity.Product;
import com.nghiasoftware.bookshop_product.mapper.ProductMapper;
import com.nghiasoftware.bookshop_product.payload.request.CreateProductRequest;
import com.nghiasoftware.bookshop_product.payload.request.SearchProductRequest;
import com.nghiasoftware.bookshop_product.repository.ProductRepository;
import com.nghiasoftware.bookshop_product.services.FileStorageServices;
import com.nghiasoftware.bookshop_product.services.ProductServices;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
        if(productRequest.getFiles() != null && productRequest.getFiles().length > 0) {
            for(MultipartFile file : productRequest.getFiles()) {
                fileStorageServices.save(file);
                images.append(file.getOriginalFilename()).append(",");
            }
            images.deleteCharAt(images.length() - 1);
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
