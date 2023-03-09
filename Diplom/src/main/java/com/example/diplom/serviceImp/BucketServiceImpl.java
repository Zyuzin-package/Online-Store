package com.example.diplom.serviceImp;

import com.example.diplom.dao.BucketRepository;
import com.example.diplom.dao.ProductRepository;
import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.Product;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.BucketDTO;
import com.example.diplom.dto.BucketDetailDTO;
import com.example.diplom.dto.DiscountDTO;
import com.example.diplom.mapper.ProductMapper;
import com.example.diplom.service.BucketService;
import com.example.diplom.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class BucketServiceImpl implements BucketService {
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final DiscountServiceImpl discountService;
    private final UserService userService;

    private final ProductMapper mapper = ProductMapper.MAPPER;

    public BucketServiceImpl(BucketRepository bucketRepository, ProductRepository productRepository, DiscountServiceImpl discountService, UserService userService) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
        this.discountService = discountService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Bucket createBucket(UserM userM, List<Long> productsIds) {
        Bucket bucket = new Bucket();
        bucket.setUser(userM);
        List<Product> productList = getCollectRefProductsByIds(productsIds);
        bucket.setProducts(productList);
        return bucket;
    }

    private List<Product> getCollectRefProductsByIds(List<Long> productIds) {
        return productIds.stream()
                .map(productRepository::getOne)
                .collect(Collectors.toList());
    }

    @Override
    public void addProduct(Bucket bucket, List<Long> productsIds) {
        List<Product> products = bucket.getProducts();
        List<Product> newProductList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductsByIds(productsIds));
        bucket.setProducts(newProductList);
        bucketRepository.save(bucket);
    }

    @Override
    public BucketDTO removeProduct(Bucket bucket, Long productId, String name) {
        BucketDTO bucketDTO = getBucketByUser(name);

        List<BucketDetailDTO> bucketDetails = bucketDTO.getBucketDetails();
        for (BucketDetailDTO b : bucketDetails) {
            if (Objects.equals(b.getProductId(), productId)) {
                bucketDetails.remove(b);
                break;
            }
        }
        bucketDTO.setBucketDetails(bucketDetails);
        bucketDTO.aggregate();


        List<Product> products = bucket.getProducts();
        for (Product p : products) {
            if (Objects.equals(p.getId(), productId)) {
                String productName = p.getTitle();
                products.removeIf(p2 -> p2.getTitle().equals(productName));
                break;
            }
        }
        bucket.setProducts(products);
        save(bucket);
        return bucketDTO;
    }

    @Override
    public void removeProductByUsername(Long productId, String username) {
        bucketRepository.removeProductByUsername(productId, username);
    }

    @Override
    public void clearBucket(Bucket bucket, String name) {
        if (bucket == null) {
            throw new RuntimeException("You bucket is empty");
        }
        List<Product> products = bucket.getProducts();
        products.clear();
        bucket.setProducts(products);
        bucketRepository.save(bucket);
    }

    @Override
    public void amountIncrease(Bucket bucket, Long productId) {

        addProduct(bucket, List.of(productId));

    }

    @Override
    public void amountDecrease(Bucket bucket, Long productId) {
        if (bucket == null) {
            throw new RuntimeException("You bucket is empty");
        }

        List<Product> products = bucket.getProducts();
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                products.remove(p);
                break;
            }
        }

        bucket.setProducts(products);
        bucketRepository.save(bucket);
    }

    @Override
    public void save(Bucket bucket) {
        bucketRepository.save(bucket);
    }

    @Override
    public BucketDTO getBucketByUser(String name) {
        UserM user = userService.findByName(name);
        if (user == null || user.getBucket() == null) {
            return new BucketDTO();
        }

        BucketDTO bucketDTO = new BucketDTO();
        Map<Long, BucketDetailDTO> mapByProductId = new HashMap<>();

        List<Product> products = user.getBucket().getProducts();
        List<DiscountDTO> discounts = discountService.findDiscountsByProducts(mapper.fromProductList(products));

        products.sort(Comparator.comparing(Product::getId));
        discounts.sort(Comparator.comparing(DiscountDTO::getProduct_id));

        for (int i = 0; i <= products.size() - 1; i++) {
            BucketDetailDTO detail = mapByProductId.get(products.get(i).getId());
            if (detail == null) {
                BucketDetailDTO bucketDetailDTO = new BucketDetailDTO(products.get(i));

                bucketDetailDTO.setDiscountPrice(discounts.get(i).getDiscount_price());

                mapByProductId.put(products.get(i).getId(), bucketDetailDTO);
            } else {
                detail.setAmount(detail.getAmount() + 1);
                if (detail.getDiscountPrice() > 0) {
                    detail.setSum(discounts.get(i).getDiscount_price() * detail.getAmount());
                } else {
                    detail.setSum(detail.getPrice() * detail.getAmount());
                }
            }
        }
        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();
        bucketDTO.setUserId(user.getId());
        return bucketDTO;
    }

    @Override
    public boolean checkBucketProducts(Long id) {
        Bucket bucket = bucketRepository.getBucketByUserId(id);
        if (bucket == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkProductInAllBuckets(Long productId){
        Product product = bucketRepository.getProductFromBucketById(productId);
        if(product == null){
            return true;
        } else {
            return false;
        }

    }

}
