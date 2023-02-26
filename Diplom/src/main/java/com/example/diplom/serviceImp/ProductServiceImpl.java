package com.example.diplom.serviceImp;

import com.example.diplom.dao.ProductRepository;
import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.Product;
import com.example.diplom.domain.UserM;
import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.dto.CategoryDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.*;
import com.example.diplom.service.statistics.FrequencyAddToCartStatsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final UserService userService;
    private final BucketService bucketService;
    private final CategoryService categoryService;

    private final ProductRepository productRepository;
    private final UserNotificationService userNotificationService;
    private final DiscountService discountService;
    private final ImageService imageService;
    private final FrequencyAddToCartStatsService frequencyAddToCartStatsService;

    public ProductServiceImpl(UserService userService, BucketService bucketService, CategoryService categoryService, ProductRepository productRepository, UserNotificationService userNotificationService, DiscountService discountService, ImageService imageService, FrequencyAddToCartStatsService frequencyAddToCartStatsService) {
        this.userService = userService;
        this.bucketService = bucketService;
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.userNotificationService = userNotificationService;
        this.discountService = discountService;
        this.imageService = imageService;
        this.frequencyAddToCartStatsService = frequencyAddToCartStatsService;
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
            userService.saveBucket(newBucket, userM);
        } else {
            bucketService.addProduct(bucket, Collections.singletonList(productId));
        }
        Product product = productRepository.findFirstById(productId);
        userNotificationService.sendNotificationToUser(UserNotificationDTO.builder()
                .message("Product: " + product.getTitle() + " was added to you bucket")
                .url("")
                .urlText("")
                .userId(userM.getId())
                .build());
        frequencyAddToCartStatsService.save(FrequencyAddToCartStats.builder()
                .product(product)
                .build());
    }

    @Override
    public boolean save(ProductDTO productDTO, MultipartFile file, String category, Double discount) {
        Product savedProduct = productRepository.findByTitle(productDTO.getTitle());

        String imageName = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String path = "/img/" + imageName + ".jpg";

        if (savedProduct == null) {
            try {
                Product newProduct = Product.builder()
                        .price(productDTO.getPrice())
                        .categories(new ArrayList<>())
                        .title(productDTO.getTitle())
                        .description(productDTO.getDescription())
                        .image(productDTO.getImage())
                        .build();
                System.out.println("1");
                if (!file.isEmpty()) {
                    if (imageService.saveImage(file, imageName, path, category)) {
                        newProduct.setImage(path);
                        System.out.println("2");
                    } else {
                        return false;
                    }
                }
                productRepository.save(newProduct);
                if (discount <= 0) {
                    discountService.save(0, getProductByName(productDTO.getTitle()).getId());
                } else {
                    if (discount < productDTO.getPrice()) {
                        discountService.save(discount, getProductByName(productDTO.getTitle()).getId());
                    } else {
                        System.out.println("FALSE");
                        return false;
                    }

                }
                System.out.println("6");

                addCategoryToProduct(category, productDTO);
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
            if (productDTO.getPrice() != 0
                    && !Objects.equals(productDTO.getPrice(), savedProduct.getPrice())) {

                savedProduct.setPrice(productDTO.getPrice());
                isChanged = true;
            }
            if (!Objects.equals(productDTO.getDescription(), savedProduct.getDescription())) {
                savedProduct.setDescription(productDTO.getDescription());
                isChanged = true;
            }

            if (!file.isEmpty()) {
                imageService.removeImage(savedProduct.getImage());
                if (imageService.saveImage(file, imageName, path, category)) {
                    isChanged = true;
                    savedProduct.setImage(path);
                }
            }

            if (discount <= 0) {
                discountService.save(0, getProductByName(productDTO.getTitle()).getId());

            } else {
                if (discount < productDTO.getPrice()) {
                    discountService.save(discount, getProductByName(productDTO.getTitle()).getId());
                }
            }

            if (isChanged) {
                addCategoryToProduct(category, productDTO);
                productRepository.save(savedProduct);
            }
        }
        return true;
    }

    @Override
    public boolean changeName(ProductDTO productDTO, String oldName) {
        ProductDTO product = getProductByName(oldName);

        product.setTitle(productDTO.getTitle());

        Product newProduct = Product.builder()
                .price(productDTO.getPrice())
                .categories(new ArrayList<>())
                .title(productDTO.getTitle())
                .description(productDTO.getDescription())
                .image(productDTO.getImage())
                .id(productDTO.getId())
                .build();
        removeWithOutPhoto(productDTO.getId());
        productRepository.save(newProduct);

        return false;
    }

    @Override
    public boolean removeWithPhoto(Long productId) {
        Product product = findProductById(productId);
        System.out.println(product);
        if (product == null) {
            return false;
        }
        if (!imageService.removeImage(product.getImage())) {
            return false;
        }
        productRepository.deleteById(product.getId());
        return true;
    }

    @Override
    public boolean removeWithOutPhoto(Long productId) {
        Product product = findProductById(productId);
        if (product == null) {
            return false;
        }
        productRepository.deleteById(product.getId());
        return true;
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
    public ProductDTO getProductByName(String name) {
        Product product = productRepository.findByTitle(name);
        if (product == null) {
            return null;
        } else {
            return new ProductDTO(product);
        }
    }

    @Override
    public List<ProductDTO> getProductsByBucketId(Long bucketId) {
        List<Long> productsIds = productRepository.getProductIdsByBucketId(bucketId);
        List<Product> products = new ArrayList<>();
        for (Long l : productsIds) {
            products.add(findProductById(l));
        }
        return mapper.fromProductList(products);
    }

    @Override
    public List<ProductDTO> getProductsByUserIds(Long id) {
        return mapper.fromProductList(productRepository.getProductsByUserIds(id));
    }

    @Override
    public boolean removeProductsByCategoryName(String title) {
        try {
            List<ProductDTO> products = getProductsByCategory(title);
            for (ProductDTO productDTO : products) {
                productRepository.deleteById(productDTO.getId());
            }
            productRepository.removeFromProductsToCategoryByCategoryName(title);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }


}
