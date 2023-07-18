package com.example.diplom.service;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


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

    boolean removeProductsByCategoryName(String title);


}
