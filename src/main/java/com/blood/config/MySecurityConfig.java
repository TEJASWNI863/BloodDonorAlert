package com.blood.config;






import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import jakarta.servlet.DispatcherType;


@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.home.controllers","com.home.service","com.home.model","com.blood.config","com.home.DAO","com.email"})
public class MySecurityConfig{
	@Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
            		.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE).permitAll()
            		.requestMatchers("/bloodRequest").authenticated()
            		.requestMatchers("/blood-request.html").authenticated()
            		
            		
            		.anyRequest().permitAll())
            .formLogin(form -> form
            		.loginPage("/LoginPage.html")
            		.loginProcessingUrl("/login")
            		.failureUrl("/LoginPage.html?error=true") 
            		.defaultSuccessUrl("/bloodRequest", true)
            		
            		.permitAll())
        
        
        .logout(logout -> logout
                .permitAll()
            );
        return http.build();
    	 
      }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
	public DataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		 String dbUrl = System.getenv("DATABASE_URL");
		    String dbUser = System.getenv("DB_USERNAME");
		    String dbPassword = System.getenv("DB_PASSWORD");
		    
		    // Fallback to local values if environment variables not set
		    if (dbUrl == null) {
		        dbUrl = "jdbc:mysql://localhost:3306/blooddonors";
		        dbUser = "root";
		        dbPassword = "srinivasarao@123";
		    }
		    
		    driverManagerDataSource.setUsername(dbUser);
		    driverManagerDataSource.setPassword(dbPassword);
		    driverManagerDataSource.setUrl(dbUrl);
		
		driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		return driverManagerDataSource;
	}
    @Bean
    public JdbcTemplate jdbcTemplate() {
    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
    	
    	return jdbcTemplate;
    }
    
    
    
    
    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    
}
