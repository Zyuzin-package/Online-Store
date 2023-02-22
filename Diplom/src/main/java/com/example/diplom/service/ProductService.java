package com.example.diplom.service;

import com.example.diplom.domain.Product;
import com.example.diplom.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();

    void addToUserBucket(Long productId, String username);

    boolean save(ProductDTO productDTO, MultipartFile file, String category);

    boolean remove(Long productId);

    Product findProductById(Long productId);

    List<ProductDTO> getProductsByCategory(String category);

    void addCategoryToProduct(String categoryName, ProductDTO product);

    ProductDTO getProductByName(String name);

    List<ProductDTO> getProductsByBucketId(Long bucketId);

    List<ProductDTO> getProductsByUserIds(Long id);

    void removeProductsByCategoryName(String title);

    boolean saveImage(MultipartFile file, String name, String path,String category);
}
