//package com.example.diplom.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.hibernate5.HibernateTransactionManager;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
//
//import javax.sql.DataSource;
//import java.beans.PropertyVetoException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Properties;
//
//@Configuration
//public class HibernateConfig {
//    private Properties hibernateProperties;
//    public HibernateConfig() throws IOException {
//        hibernateProperties = new Properties();
//        hibernateProperties.load(new FileReader("C:/hibernate.properties"));
//    }
//
//    @Bean
//    public LocalSessionFactoryBean getSessionFactory() throws PropertyVetoException {
//        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
//        System.out.println("korka");
//        sessionFactoryBean.setHibernateProperties(hibernateProperties);
//        sessionFactoryBean.setDataSource(getDataSource());
//        sessionFactoryBean.setPackagesToScan("com.example.diplom");
//
//        return sessionFactoryBean;
//    }
//
//    @Bean
//    public DataSource getDataSource() throws PropertyVetoException {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//
//        dataSource.setDriverClassName(hibernateProperties.getProperty("connection.driver"));
//        dataSource.setUrl(hibernateProperties.getProperty("connection.url"));
//        dataSource.setUsername(hibernateProperties.getProperty("connection.user"));
//        dataSource.setPassword(hibernateProperties.getProperty("connection.password"));
//
//        return dataSource;
//    }
//
//    @Bean
//    public HibernateTransactionManager getTransactionManager() throws PropertyVetoException {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(getSessionFactory().getObject());
//        return transactionManager;
//    }
//
//}
