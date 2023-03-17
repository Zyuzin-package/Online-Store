package com.example.diplom.service;

import com.example.diplom.domain.Product;
import com.example.diplom.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();

    void addToUserBucket(Long productId, String username);

    boolean save(ProductDTO productDTO, MultipartFile file, String category,Double discount);
    boolean changeName(ProductDTO productDTO, String oldName, String category, MultipartFile file,Double discount);
    boolean removeWithPhoto(Long productId);

    Product findProductById(Long productId);

    List<ProductDTO> getProductsByCategory(String category);

    void addCategoryToProduct(String categoryName, ProductDTO product);

    ProductDTO getProductByName(String name);

    List<ProductDTO> getProductsByBucketId(Long bucketId);

    List<ProductDTO> getProductsByUserIds(Long id);

    boolean removeProductsByCategoryName(String title);


}
