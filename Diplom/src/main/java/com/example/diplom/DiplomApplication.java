package com.example.diplom;

import org.flywaydb.core.Flyway;
import org.hibernate.Hibernate;
import org.hibernate.Session;
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

        Flyway flyway = Flyway.configure().dataSource(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "root")
                .load();
        // Start the migration
        flyway.migrate();


        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        System.out.println(encoder.encode("pass"));
    }

}
