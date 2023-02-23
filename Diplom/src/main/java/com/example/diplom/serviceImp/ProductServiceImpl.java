package com.example.diplom.serviceImp;

import com.example.diplom.dao.ProductRepository;
import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.Product;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.CategoryDTO;
import com.example.diplom.dto.ProductDTO;
import com.example.diplom.dto.UserNotificationDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
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


    public ProductServiceImpl(UserService userService, BucketService bucketService, CategoryService categoryService, ProductRepository productRepository, UserNotificationService userNotificationService, DiscountService discountService) {
        this.userService = userService;
        this.bucketService = bucketService;
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.userNotificationService = userNotificationService;
        this.discountService = discountService;
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
            userService.saveBucket(newBucket,userM);
        } else {
            bucketService.addProduct(bucket, Collections.singletonList(productId));
        }

        userNotificationService.sendNotificationToUser(UserNotificationDTO.builder()
                .message("Product: "+ productRepository.findFirstById(productId).getTitle()+" was added to you bucket")
                .url("")
                .urlText("")
                .userId(userM.getId())
                .build());
    }

    @Override
    public boolean save(ProductDTO productDTO, MultipartFile file, String category) {
        Product savedProduct = productRepository.findByTitle(productDTO.getTitle());

        String imageName = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String path = "/img/" + imageName + ".jpg";

        if (savedProduct == null) {
//            if (productDTO.getPrice() <= 0) {
//                return false;TODO: Это должно быть лишним
//            }
            try {
                Product newProduct = Product.builder()
                        .price(productDTO.getPrice())
                        .categories(new ArrayList<>())
                        .title(productDTO.getTitle())
                        .description(productDTO.getDescription())
                        .image(productDTO.getImage())
                        .build();


                if (saveImage(file, imageName, path, category)) {
                    newProduct.setImage(path);

                    Product product = productRepository.save(newProduct);
                    discountService.save(0, product.getId());
                    return true;
                } else {
                    return false;
                }
            } catch (RuntimeException e) {
                return false;
            }
        } else {
            boolean isChanged = false;
            if (!Objects.equals(productDTO.getTitle(), savedProduct.getTitle())) {
                savedProduct.setTitle(productDTO.getTitle());
                isChanged = true;
            }
            if (productDTO.getPrice()!=0
                    && !Objects.equals(productDTO.getPrice(), savedProduct.getPrice())) {

                savedProduct.setPrice(productDTO.getPrice());
                isChanged = true;
            }
            if (!Objects.equals(productDTO.getDescription(), savedProduct.getDescription())) {
                savedProduct.setDescription(productDTO.getDescription());
                isChanged = true;
            }

            if (!file.isEmpty()) {
                removeImage(savedProduct.getImage());
                if (saveImage(file, imageName, path, category)) {
                    isChanged = true;
                    savedProduct.setImage(path);
                }
            }
            if (isChanged) {
                productRepository.save(savedProduct);
            }

        }
        discountService.save(0, savedProduct.getId());
        return true;
    }

    @Override
    public boolean remove(Long productId) {
        Product product = findProductById(productId);
        if (!removeImage(product.getImage())) {
            return false;
        }
        productRepository.deleteProductIdById(productId);
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
        return new ProductDTO(productRepository.findByTitle(name));
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
    public void removeProductsByCategoryName(String title) {
        List<ProductDTO> products = getProductsByCategory(title);
        for (ProductDTO productDTO : products) {
            productRepository.deleteById(productDTO.getId());
        }
        productRepository.removeFromProductsToCategoryByCategoryName(title);
    }

    @Override
    public boolean saveImage(MultipartFile file, String name, String path, String category) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/" + path)));
                stream.write(bytes);
                stream.close();

                System.out.println("Successful load file");
                return true;
            } catch (Exception e) {
                System.out.println("Error load file" + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("File is empty");
            return false;
        }
    }

    @Override
    public boolean removeImage(String imageUrl) {
        String path = "src/main/resources/static/" + imageUrl;
        File file = new File(path);
        if (!file.delete()) {
            System.out.println("\n\nError delete");
            return false;
        }
        return true;
    }
}
