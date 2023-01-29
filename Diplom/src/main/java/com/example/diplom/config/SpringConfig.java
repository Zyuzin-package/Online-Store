//package com.example.diplom.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//
//@Configuration
//@ComponentScan({"com.example.diplom"})
//@EnableWebMvc
//@EnableTransactionManagement
//public class SpringConfig implements WebMvcConfigurer {
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(stringConverter());
//        converters.add(jacksonConverter());
//
//        WebMvcConfigurer.super.configureMessageConverters(converters);
//    }
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper().registerModule(new JavaTimeModule())
//                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//    }
//
//    @Bean
//    public MappingJackson2HttpMessageConverter jacksonConverter() {
//        return new MappingJackson2HttpMessageConverter();
//    }
//
//    @Bean
//    public StringHttpMessageConverter stringConverter() {
//        return new StringHttpMessageConverter();
//    }
//
//}
