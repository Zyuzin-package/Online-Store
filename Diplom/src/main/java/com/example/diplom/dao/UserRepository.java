package com.example.diplom.dao;


import com.example.diplom.domain.UserM;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@EnableJpaRepositories("com.example.diplom.domains")
@ComponentScan(basePackages = { "com.example.diplom.domains" })
@EntityScan("com.example.diplom.domains")
public interface UserRepository extends JpaRepository<UserM,Long> {
    @Transactional
    UserM findFirstByName(String name);
    @Transactional
    @Modifying
    @Query(value ="UPDATE public.users SET name = :name WHERE id = :id", nativeQuery = true)
    void updateUserByName(@Param("name")String name, @Param("id")long id);
}
