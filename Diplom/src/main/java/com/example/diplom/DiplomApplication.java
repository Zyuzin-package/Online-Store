package com.example.diplom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories
public class DiplomApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =  SpringApplication.run(DiplomApplication.class, args);
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        System.out.println(encoder.encode("pass"));
    }

}
