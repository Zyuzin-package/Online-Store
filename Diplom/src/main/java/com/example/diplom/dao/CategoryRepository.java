package com.example.diplom.dao;

import com.example.diplom.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Transactional
    Category findByTitle(String title);

    @Modifying
    @Query(value = "delete from categories where title=:name", nativeQuery = true)
    @Transactional
    void removeByTitle(@Param("name") String name);
}
