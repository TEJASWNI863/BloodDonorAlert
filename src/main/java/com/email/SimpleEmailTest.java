package com.email;

import com.home.model.BloodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SimpleEmailTest {

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${MAIL_FROM:tejaswinipotnuru1@gmail.com}")
    private String fromEmail;

    public void sendEmail(String email, BloodRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Urgent Blood Requirement - " + request.getRequiredBloodType());

            // Create detailed email body
            String emailBody = buildEmailBody(request);
            message.setText(emailBody);

            mailSender.send(message);
            System.out.println("✅ Email sent successfully to: " + email);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String buildEmailBody(BloodRequest request) {
        StringBuilder body = new StringBuilder();
        body.append("URGENT BLOOD REQUIREMENT\n");
        body.append("========================\n\n");
        body.append("Dear Donor,\n\n");
        body.append("We have an urgent requirement for blood donation. Below are the details:\n\n");

        body.append("Patient Information:\n");
        body.append("-------------------\n");
        body.append("Patient Name: ").append(request.getPatientName()).append("\n");
        if (request.getPatientAge() != null) {
            body.append("Age: ").append(request.getPatientAge()).append(" years\n");
        }
        body.append("Required Blood Group: ").append(request.getRequiredBloodType()).append("\n");
        body.append("Urgency Level: ").append(request.getUrgencyLevel()).append("\n\n");

        body.append("Hospital Details:\n");
        body.append("----------------\n");
        body.append("Hospital Name: ").append(request.getHospital()).append("\n");
        body.append("Hospital State: ").append(request.getHospitalState()).append("\n");
        body.append("Hospital District: ").append(request.getHospitalDistrict()).append("\n");
        body.append("Hospital City: ").append(request.getHospitalCity()).append("\n\n");
        
        body.append("Contact Information:\n");
        body.append("-------------------\n");
        body.append("Contact Person: ").append(request.getContactPerson()).append("\n");
        body.append("Contact Phone: ").append(request.getContactPhone()).append("\n");
        if (request.getContactEmail() != null && !request.getContactEmail().isEmpty()) {
            body.append("Contact Email: ").append(request.getContactEmail()).append("\n");
        }

        body.append("\n");
        body.append("Your donation can save a life. Please contact us immediately if you can help.\n\n");
        body.append("Thank you for your support!\n\n");
        body.append("Best regards,\n");
        body.append("Blood Donation Team");

        return body.toString();
    }
}