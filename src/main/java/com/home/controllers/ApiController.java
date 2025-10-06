package com.home.controllers;

import com.home.DAO.BloodRequestDAO;
import com.home.DAO.DonorDAO;
import com.home.model.BloodRequest;
import com.home.model.Donor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.email.SimpleEmailTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private BloodRequestDAO bloodRequestDAO;

    @Autowired
    private DonorDAO donorDAO;
    
    @Autowired
    private SimpleEmailTest emailService;  // ✅ ADD THIS - Let Spring inject it

    @GetMapping("/blood-request/{id}")
    public ResponseEntity<BloodRequest> getBloodRequest(@PathVariable("id") Long id) {
        BloodRequest request = bloodRequestDAO.findById(id);
        if (request == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(request);
    }

    @GetMapping("/matching-donors")
    public ResponseEntity<Map<String, Object>> getMatchingDonors(
            @RequestParam("bloodType") String bloodType,
            @RequestParam(value = "requestId", required = false) Long requestId) {

        // Get the actual blood request if requestId is provided
        BloodRequest bloodRequest = null;
        List<Donor> matchingDonors;
        
        if (requestId != null) {
            bloodRequest = bloodRequestDAO.findById(requestId);
            
            // If we have the blood request with location, filter by location too
            if (bloodRequest != null && 
                bloodRequest.getHospitalCity() != null && !bloodRequest.getHospitalCity().trim().isEmpty() &&
                bloodRequest.getHospitalState() != null && !bloodRequest.getHospitalState().trim().isEmpty()) {
                
                // Match by blood type, city, AND state
                matchingDonors = donorDAO.findByBloodTypeAndLocation(
                    bloodType,
                    bloodRequest.getHospitalCity(),
                    bloodRequest.getHospitalState()
                );
                
                System.out.println("Filtering donors by blood type: " + bloodType + 
                    ", city: " + bloodRequest.getHospitalCity() + 
                    ", state: " + bloodRequest.getHospitalState());
                System.out.println("Found " + matchingDonors.size() + " matching donors");
            } else {
                // Fallback to blood type only if location not available
                matchingDonors = donorDAO.findByBloodType(bloodType);
                System.out.println("Filtering donors by blood type only (no location data available)");
            }
        } else {
            // No request ID, just filter by blood type
            matchingDonors = donorDAO.findByBloodType(bloodType);
            System.out.println("No request ID provided, filtering by blood type only");
        }

        // Send emails to matching donors (now filtered by location)
        if (bloodRequest != null && matchingDonors != null && !matchingDonors.isEmpty()) {
            // ❌ REMOVE THIS LINE:
            // SimpleEmailTest emailSender = new SimpleEmailTest();
            
            int emailsSent = 0;
            for (Donor donor : matchingDonors) {
                try {
                    // ✅ USE THE INJECTED SERVICE:
                    emailService.sendEmail(donor.getEmail(), bloodRequest);
                    emailsSent++;
                    System.out.println("Email sent to: " + donor.getEmail() + 
                        " (" + donor.getFirstName() + " " + donor.getLastName() + ") in " + 
                        donor.getCity() + ", " + donor.getState());
                } catch (Exception e) {
                    System.err.println("Failed to send email to " + donor.getEmail() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            System.out.println("Total emails sent: " + emailsSent + " out of " + matchingDonors.size() + " donors");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("donors", matchingDonors);
        response.put("count", matchingDonors.size());

        if (bloodRequest != null) {
            response.put("request", bloodRequest);
        }

        return ResponseEntity.ok(response);
    }
}