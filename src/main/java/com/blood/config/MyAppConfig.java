package com.blood.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.home.controllers","com.home.service","com.home.model","com.home.DAO","com.email"})
public class MyAppConfig implements WebMvcConfigurer {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/view/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public DataSource dataSource() {
        System.out.println("========================================");
        System.out.println("🔵 CREATING DATABASE CONNECTION");
        System.out.println("========================================");
        
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();

        // Get environment variables
        String dbHost = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_NAME");
        String dbUser = System.getenv("DB_USERNAME");
        String dbPassword = System.getenv("DB_PASSWORD");

        // Print what we got from environment
        System.out.println("DB_HOST from env: " + dbHost);
        System.out.println("DB_PORT from env: " + dbPort);
        System.out.println("DB_NAME from env: " + dbName);
        System.out.println("DB_USERNAME from env: " + dbUser);
        System.out.println("DB_PASSWORD from env: " + (dbPassword != null ? "***SET***" : "NULL"));

        // Fallback to local values if environment variables not set
        if (dbHost == null || dbHost.isEmpty()) {
            System.out.println("⚠️  Environment variables not found, using LOCAL database");
            dbHost = "localhost";
            dbPort = "3306";
            dbName = "blooddonors";
            dbUser = "root";
            dbPassword = "srinivasarao@123";
        } else {
            System.out.println("✅ Using REMOTE database (Aiven)");
        }

        // Build the database URL with SSL
        String dbUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName 
                       + "?useSSL=true&requireSSL=true&verifyServerCertificate=false";

        System.out.println("Final DB URL: " + dbUrl);
        System.out.println("Final DB User: " + dbUser);
        System.out.println("========================================");

        driverManagerDataSource.setUsername(dbUser);
        driverManagerDataSource.setPassword(dbPassword);
        driverManagerDataSource.setUrl(dbUrl);
        driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Test the connection
        try {
            java.sql.Connection conn = driverManagerDataSource.getConnection();
            System.out.println("✅✅✅ DATABASE CONNECTION SUCCESSFUL! ✅✅✅");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌❌❌ DATABASE CONNECTION FAILED! ❌❌❌");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return driverManagerDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}