package com.example.diplom.dao;

import com.example.diplom.domain.Category;
import com.example.diplom.domain.Product;
import com.example.diplom.domain.UserM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Transactional
    Product findFirstById(Long productId);

    @Transactional
    List<Product> findProductIdByCategoriesId(Long categories_id);

    @Transactional
    void deleteProductIdById(Long productId);


}
