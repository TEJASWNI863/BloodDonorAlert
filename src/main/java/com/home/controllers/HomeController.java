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
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            Long requestId = bloodRequestDAO.save(bloodRequest);
            bloodRequest.setId(requestId);

            System.out.println("Blood Request saved with ID: " + requestId);

            // Find matching donors based on blood type, city, district, AND state
            List<Donor> matchingDonors;
            if (bloodRequest.getHospitalCity() != null && !bloodRequest.getHospitalCity().trim().isEmpty() &&
                bloodRequest.getHospitalDistrict() != null && !bloodRequest.getHospitalDistrict().trim().isEmpty() &&
                bloodRequest.getHospitalState() != null && !bloodRequest.getHospitalState().trim().isEmpty()) {

                // Match by blood type, city, district, and state
                matchingDonors = donorDAO.findByBloodTypeAndLocation(
                    bloodRequest.getRequiredBloodType(),
                    bloodRequest.getHospitalCity(),
                    bloodRequest.getHospitalDistrict(),
                    bloodRequest.getHospitalState()
                );
                System.out.println("Found " + matchingDonors.size() + " matching donors in "
                    + bloodRequest.getHospitalCity() + ", " + bloodRequest.getHospitalDistrict()
                    + ", " + bloodRequest.getHospitalState());
            } else {
                // Fallback: match by blood type only if location not fully provided
                matchingDonors = donorDAO.findByBloodType(bloodRequest.getRequiredBloodType());
                System.out.println("Found " + matchingDonors.size() + " matching donors (location not fully specified)");
            }

            redirectAttributes.addFlashAttribute("bloodRequest", bloodRequest);
            redirectAttributes.addFlashAttribute("matchingDonors", matchingDonors);
            redirectAttributes.addFlashAttribute("message", "Blood request submitted successfully!");

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

    @PostMapping("/submitDonorRegistration")
    public String submitDonorRegistration(@ModelAttribute Donor donor,
                                          RedirectAttributes redirectAttributes) {
        try {
            if (donorService.usernameExists(donor.getUsername())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                    "Username already exists. Please choose a different username.");
                return "redirect:/register.html";
            }

            if (donorService.emailExists(donor.getEmail())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                    "Email already registered. Please use a different email.");
                return "redirect:/register.html";
            }

            donorService.registerDonor(donor);

            System.out.println("Donor Registration Received: " + donor);
            redirectAttributes.addAttribute("firstName", donor.getFirstName());
            redirectAttributes.addAttribute("lastName", donor.getLastName());
            redirectAttributes.addAttribute("email", donor.getEmail());
            redirectAttributes.addAttribute("phone", donor.getPhone());
            redirectAttributes.addAttribute("bloodType", donor.getBloodType());
            redirectAttributes.addAttribute("city", donor.getCity());
            redirectAttributes.addAttribute("district", donor.getDistrict());  // NEW
            redirectAttributes.addAttribute("state", donor.getState());

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