package com.example.diplom.serviceImp;

import com.example.diplom.dao.CategoryRepository;
import com.example.diplom.dao.ProductRepository;
import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.Category;
import com.example.diplom.domain.Product;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.CategoryDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.BucketService;
import com.example.diplom.service.CategoryService;
import com.example.diplom.service.ProductService;
import com.example.diplom.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final UserService userService;
    private final BucketService bucketService;
    private final CategoryService categoryService;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(UserService userService, BucketService bucketService, ProductRepository productRepository, CategoryService categoryService, CategoryRepository categoryRepository) {
        this.userService = userService;
        this.bucketService = bucketService;
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductDTO> getAll() {
        return mapper.fromProductList(productRepository.findAll());
    }

    @Override
    @Transactional
    public void addToUserBucket(Long productId, String username) {
        UserM userM = userService.findByName(username);
        if (userM == null) {
            throw new RuntimeException("User - " + username + " not found");
        }

        Bucket bucket = userM.getBucket();
        if (bucket == null) {
            Bucket newBucket = bucketService.createBucket(userM, Collections.singletonList(productId));
            bucketService.save(newBucket);
            userM.setBucket(newBucket);
            userService.save(userM);
        } else {
            bucketService.addProduct(bucket, Collections.singletonList(productId));
        }
    }

    @Override
    public boolean save(ProductDTO productDTO) {
        Product savedProduct = productRepository.findByTitle(productDTO.getTitle());
        if (savedProduct == null) {
            try {
                Product newProduct = Product.builder()
                        .price(productDTO.getPrice())
                        .categories(new ArrayList<>()) //TODO: Заменить!
                        .title(productDTO.getTitle())
                        .description(productDTO.getDescription())
                        .image(productDTO.getImage())
                        .build();
                System.out.println("Domain : " + newProduct);
                productRepository.save(newProduct);
                return true;
            } catch (RuntimeException e) {
                return false;
            }
        } else {
            boolean isChanged = false;
            if (!Objects.equals(productDTO.getTitle(), savedProduct.getTitle())) {
                savedProduct.setTitle(productDTO.getTitle());
                isChanged = true;
            }
            if (productDTO.getPrice() != null
                    && !Objects.equals(productDTO.getPrice(), BigDecimal.ZERO)
                    && Objects.equals(productDTO.getPrice(), savedProduct.getPrice())) {
                savedProduct.setPrice(productDTO.getPrice());
                isChanged = true;
            }
            if (!Objects.equals(productDTO.getDescription(), savedProduct.getDescription())) {
                savedProduct.setDescription(productDTO.getDescription());
                isChanged = true;
            }

            if (!Objects.equals(productDTO.getImage(), savedProduct.getImage())) {
                savedProduct.setImage(productDTO.getImage());
                isChanged = true;
            }
            if (isChanged) {
                productRepository.save(savedProduct);
            }
        }
        return true;
    }

    @Override
    public Category remove(Long productId) {
        Product product = findProductById(productId);
        productRepository.deleteProductIdById(productId);
        productRepository.deleteById(product.getId());

        return null;
    }

    @Override
    public Product findProductById(Long productId) {
        return productRepository.findFirstById(productId);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String category) {
        CategoryDTO categoryByName = categoryService.getCategoryByName(category);
        List<Product> products = productRepository.findProductIdByCategoriesId(categoryByName.getId());
        return mapper.fromProductList(products);
    }

    @Override
    public void addCategoryToProduct(String categoryName, ProductDTO product) {
        CategoryDTO category = categoryService.getCategoryByName(categoryName);
        ProductDTO product1 = getProductByName(product.getTitle());
        if (category != null) {
            productRepository.removeCategoryByProduct(product1.getId());
        }
        productRepository.addCategoryToProduct(
                productRepository.findByTitle(product.getTitle()).getId(),
                categoryService.getCategoryByName(categoryName).getId());
    }

    @Override
    public boolean saveCategory(CategoryDTO categoryDTO) {
        if (categoryService.getCategoryByName(categoryDTO.getTitle()) == null) {
            Category category = Category.builder()
                    .title(categoryDTO.getTitle())
                    .build();
            categoryRepository.save(category);
            return true;
        }
        return false;
    }

    @Override
    public ProductDTO getProductByName(String name) {
        return new ProductDTO(productRepository.findByTitle(name));
    }

    @Override
    public List<ProductDTO> getProductsByBucketId(Long bucketId) {
        List<Long> productsIds = productRepository.getProductIdsByBucketId(bucketId);
        List<Product> products = new ArrayList<>();
        for (Long l : productsIds){
            products.add(findProductById(l));
        }
        return mapper.fromProductList(products);
    }

    @Override
    public List<ProductDTO> getProductsByUserIds(Long id){
        return mapper.fromProductList(productRepository.getProductsByUserIds(id));
    }
}
