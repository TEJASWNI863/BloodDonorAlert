package com.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    
    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("smtp.gmail.com");
        sender.setPort(587);
        sender.setUsername("potnurusrinivasarao29@gmail.com");  // CHANGE THIS
        sender.setPassword("aatyoxzmfvaylznd");     // CHANGE THIS (16-char app password)
        
        Properties props = sender.getJavaMailProperties();
        
        props.put("mail.smtp.starttls.enable", "true");
        
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        
        props.put("mail.smtp.auth", "true");  
        return sender;
    }
}