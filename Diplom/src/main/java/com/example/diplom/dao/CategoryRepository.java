package com.example.diplom.dao;

import com.example.diplom.domain.Bucket;
import com.example.diplom.domain.Category;
import com.example.diplom.domain.Product;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

@EnableJpaRepositories("com.example.diplom.domains")
@ComponentScan(basePackages = { "com.example.diplom.domains" })
@EntityScan("com.example.diplom.domains")
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Transactional
    Category findByTitle(String title);
}
