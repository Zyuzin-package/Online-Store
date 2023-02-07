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
    UserM findFirstById(Long id);
    @Modifying
    @Query(value = "update users SET role=:role where id=:id", nativeQuery = true)
    @Transactional
    void updateRole(@Param("role")String role,@Param("id")Long id);
}
