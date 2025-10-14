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
        
        // Get SMTP settings from environment variables
        String smtpHost = System.getenv("SMTP_HOST");
        String smtpPort = System.getenv("SMTP_PORT");
        String smtpUsername = System.getenv("SMTP_USERNAME");
        String smtpPassword = System.getenv("SMTP_PASSWORD");
        
        // Fallback to Gmail if not set (for local testing)
        if (smtpHost == null || smtpHost.isEmpty()) {
            smtpHost = "smtp.gmail.com";
            smtpPort = "587";
            smtpUsername = "potnurusrinivasarao29@gmail.com";
            smtpPassword = "aatyoxzmfvaylznd";
        }
        
        sender.setHost(smtpHost);
        sender.setPort(Integer.parseInt(smtpPort));
        sender.setUsername(smtpUsername);
        sender.setPassword(smtpPassword);

        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", smtpHost);
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        return sender;
    }
}