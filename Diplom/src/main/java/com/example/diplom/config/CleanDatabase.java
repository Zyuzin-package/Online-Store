//package com.example.diplom.config;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CleanDatabase implements ApplicationRunner {
//
//
//    private Flyway flyway;
//
//    @Autowired
//    public void setFlyway(Flyway flyway) {
//        this.flyway = flyway;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        flyway.clean();
//        flyway.migrate();
//    }
//
//}