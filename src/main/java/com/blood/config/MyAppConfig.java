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
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        
        // Get Railway environment variables
        String dbHost = System.getenv("MYSQLHOST");
        String dbPort = System.getenv("MYSQLPORT");
        String dbName = System.getenv("MYSQL_DATABASE");
        String dbUser = System.getenv("MYSQLUSER");
        String dbPassword = System.getenv("MYSQLPASSWORD");

        // Fallback to local values if environment variables not set
        if (dbHost == null) {
            dbHost = "localhost";
            dbPort = "3306";
            dbName = "blooddonors";
            dbUser = "root";
            dbPassword = "srinivasarao@123";
        }

        // Build the database URL
        String dbUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        driverManagerDataSource.setUsername(dbUser);
        driverManagerDataSource.setPassword(dbPassword);
        driverManagerDataSource.setUrl(dbUrl);
        driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        return driverManagerDataSource;
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}