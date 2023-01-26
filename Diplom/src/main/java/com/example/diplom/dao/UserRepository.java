package com.example.diplom.dao;


import com.example.diplom.domain.UserM;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.example.diplom.domains")
@ComponentScan(basePackages = { "com.example.diplom.domains" })
@EntityScan("com.example.diplom.domains")
public interface UserRepository extends JpaRepository<UserM,Long> {
    UserM findFirstByName(String name);
}
