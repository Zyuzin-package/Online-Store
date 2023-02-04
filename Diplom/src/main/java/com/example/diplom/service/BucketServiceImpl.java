package com.example.diplom.service;

import com.example.diplom.dao.BucketRepository;
import com.example.diplom.dao.ProductRepository;
import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.Product;
import com.example.diplom.domain.UserM;
import com.example.diplom.dto.BucketDTO;
import com.example.diplom.dto.BucketDetailDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BucketServiceImpl implements BucketService {
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public BucketServiceImpl(BucketRepository bucketRepository, ProductRepository productRepository, UserService userService) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
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
        for (Product p : products) {
            BucketDetailDTO detail = mapByProductId.get(p.getId());
            if (detail == null) {
                mapByProductId.put(p.getId(), new BucketDetailDTO(p));
            } else {
                detail.setAmount(detail.getAmount().add(new BigDecimal(1.0)));
                detail.setSum(detail.getSum() + Double.valueOf(p.getPrice().toString()));
            }
        }
        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();

        return bucketDTO;
    }

}
