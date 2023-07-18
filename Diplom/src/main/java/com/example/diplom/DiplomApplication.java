package com.example.diplom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
// http://127.0.0.1:8080/swagger-ui/#/
@SpringBootApplication
@EnableJpaRepositories
@EntityScan({"com.example.models","com.example.diplom"})
public class DiplomApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =  SpringApplication.run(DiplomApplication.class, args);
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        System.out.println(encoder.encode("pass"));
    }

}
