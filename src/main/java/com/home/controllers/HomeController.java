package com.home.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.home.DAO.BloodRequestDAO;
import com.home.DAO.DonorDAO;
import com.home.model.*;
import com.home.service.DonorService;

@Controller
public class HomeController {
	 @Autowired
	  private DonorService donorService;
	 @Autowired
	  private BloodRequestDAO bloodRequestDAO;
	 @Autowired
	 private DonorDAO donorDAO;
	@GetMapping("/")
	public String Home() {
		return "redirect:/home.html";
	}
	@GetMapping("/Login")
	public String Login() {
		return "redirect:/LoginPage.html";
	}
	
	@GetMapping("/register")
	public String Register() {
		return "redirect:/register.html";
	}
	@GetMapping("/bloodRequest")
	public String BloodRequest() {
		return "redirect:/blood-request.html";
	}
	@PostMapping("/submitBloodRequest")
    public String submitBloodRequest(@ModelAttribute BloodRequest bloodRequest,
                                     RedirectAttributes redirectAttributes) {
		try {
            // Get currently logged in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
           

            // Save blood request to database
            Long requestId = bloodRequestDAO.save(bloodRequest);
            bloodRequest.setId(requestId);
            
            System.out.println("Blood Request saved with ID: " + requestId);

         // Find matching donors based on blood type AND location
            List<Donor> matchingDonors;
            if (bloodRequest.getHospitalCity() != null && !bloodRequest.getHospitalCity().trim().isEmpty()) {
                if (bloodRequest.getHospitalState() != null && !bloodRequest.getHospitalState().trim().isEmpty()) {
                    // Match by blood type, city, and state
                    matchingDonors = donorDAO.findByBloodTypeAndLocation(
                        bloodRequest.getRequiredBloodType(), 
                        bloodRequest.getHospitalCity(),
                        bloodRequest.getHospitalState()
                    );
                    System.out.println("Found " + matchingDonors.size() + " matching donors in " 
                        + bloodRequest.getHospitalCity() + ", " + bloodRequest.getHospitalState());
                } else {
                    // Fallback: match by blood type and city only (if you have this method)
                    matchingDonors = donorDAO.findByBloodType(bloodRequest.getRequiredBloodType());
                    System.out.println("Found " + matchingDonors.size() + " matching donors (state not specified)");
                }
            } else {
                // Fallback: match by blood type only if location not provided
                matchingDonors = donorDAO.findByBloodType(bloodRequest.getRequiredBloodType());
                System.out.println("Found " + matchingDonors.size() + " matching donors (location not specified)");
            }

            // Pass data to donors list page
            redirectAttributes.addFlashAttribute("bloodRequest", bloodRequest);
            redirectAttributes.addFlashAttribute("matchingDonors", matchingDonors);
            redirectAttributes.addFlashAttribute("message", "Blood request submitted successfully!");
            
            // Also add requestId as URL parameter so it can be accessed after redirect
            
            return "redirect:/donors-list.html?requestId=" + requestId;
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to submit blood request. Please try again.");
            return "redirect:/blood-request.html";
        }
    }
	@GetMapping("/donorsList")
	public String DonorsList() {
		return "redirect:/donors-list.html";
	}
	// Handle donor registration form submission
	@PostMapping("/submitDonorRegistration")
    public String submitDonorRegistration(@ModelAttribute Donor donor,
                                          RedirectAttributes redirectAttributes) {
		try {
            // Check if username already exists
            if (donorService.usernameExists(donor.getUsername())) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Username already exists. Please choose a different username.");
                return "redirect:/register.html";
            }

            // Check if email already exists
            if (donorService.emailExists(donor.getEmail())) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Email already registered. Please use a different email.");
                return "redirect:/register.html";
            }

            // Register the donor
            donorService.registerDonor(donor);
            
            System.out.println("Donor Registration Received: " + donor);
            redirectAttributes.addAttribute("firstName", donor.getFirstName());
            redirectAttributes.addAttribute("lastName", donor.getLastName());
            redirectAttributes.addAttribute("email", donor.getEmail());
            redirectAttributes.addAttribute("phone", donor.getPhone());
            redirectAttributes.addAttribute("bloodType", donor.getBloodType());
            redirectAttributes.addAttribute("state", donor.getState());
            redirectAttributes.addAttribute("city", donor.getCity());
            
            return "redirect:/registration-success.html";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Registration failed. Please try again.");
            return "redirect:/register.html";
        }
    }
	@GetMapping("/registrationSuccess")
	public String RegistrationSuccess() {
		return "redirect:/registration-success.html";
	}
}
