package com.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${MAIL_HOST:smtp-relay.brevo.com}")
    private String mailHost;
    
    @Value("${MAIL_PORT:587}")
    private int mailPort;

    @Value("${MAIL_USERNAME:98e39f001@smtp-brevo.com}")
    private String mailUsername;

    @Value("${MAIL_PASSWORD:Ih8OMxQ1f0gNpER7}")
    private String mailPassword;
    
    @Value("${MAIL_FROM:tejaswinipotnuru1@gmail.com}")
    private String mailFrom;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(mailHost);
        sender.setPort(mailPort);
        sender.setUsername(mailUsername);
        sender.setPassword(mailPassword);

        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");

        return sender;
    }
}