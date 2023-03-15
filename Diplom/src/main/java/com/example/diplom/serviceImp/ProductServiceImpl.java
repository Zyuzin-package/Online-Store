package com.example.diplom.serviceImp;

import com.example.diplom.dao.ProductRepository;
import com.example.diplom.domain.*;
import com.example.diplom.dto.CategoryDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.*;
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


    public ProductServiceImpl(UserService userService, BucketService bucketService, CategoryService categoryService, ProductRepository productRepository, UserNotificationService userNotificationService, DiscountService discountService, ImageService imageService) {
        this.userService = userService;
        this.bucketService = bucketService;
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.userNotificationService = userNotificationService;
        this.discountService = discountService;
        this.imageService = imageService;
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
                if (!file.isEmpty()) {
                    if (imageService.saveImage(file, imageName, path, category)) {
                        newProduct.setImage(path);
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
                        return false;
                    }

                }

                addCategoryToProduct(category, productDTO);
                return true;
            } catch (RuntimeException e) {
                return false;
            }
        } else {
            if(!bucketService.checkBucketProducts(savedProduct.getId())){
                return false;
            }
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
    public boolean changeName(ProductDTO productDTO, String oldName, String category, MultipartFile file,Double discount) {
        Product product = productRepository.findByTitle(oldName);
        product.setTitle(productDTO.getTitle());

       if(!bucketService.checkBucketProducts(product.getId())){
           return false;
       }

        CategoryDTO categoryDTO = categoryService.getCategoryByName(category);

        Category category1 = Category.builder()
                .id(categoryDTO.getId())
                .title(categoryDTO.getTitle()).build();

        Product newProduct = Product.builder()
                .price(productDTO.getPrice())
                .categories(List.of(category1))
                .title(productDTO.getTitle())
                .description(productDTO.getDescription())
                .image(product.getImage())
                .id(product.getId())
                .build();

        addCategoryToProduct(category, productDTO);

        if (file.getSize() != 0) {
            String imageName = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            String path = "/img/" + imageName + ".jpg";
            imageService.saveImage(file, imageName, path, null);
            newProduct.setImage(path);
        }

        productRepository.deleteById(product.getId());
        productRepository.save(newProduct);

        if (discount <= 0) {
            discountService.save(0, getProductByName(productDTO.getTitle()).getId());
        } else {
            if (discount < productDTO.getPrice()) {
                discountService.save(discount, getProductByName(productDTO.getTitle()).getId());
            }
        }

        return true;
    }

    @Override
    public boolean removeWithPhoto(Long productId) {
        Product product = findProductById(productId);
        if (product == null) {
            return false;
        }
        productRepository.removeReviewFromProductsByProductId(productId);
        if (!imageService.removeImage(product.getImage())) {
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
                productRepository.removeReviewFromProductsByProductId(productDTO.getId());
                productRepository.deleteById(productDTO.getId());
            }
            productRepository.removeFromProductsToCategoryByCategoryName(title);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }


}
