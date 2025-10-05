package com.home.service;

import com.home.DAO.DonorDAO;
import com.home.model.Donor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DonorService {

    @Autowired
    private DonorDAO donorDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerDonor(Donor donor) {
        // Encode the password before saving
        donor.setPassword(passwordEncoder.encode(donor.getPassword()));
        donor.setEnabled(true);
        donorDAO.save(donor);
    }

    public Donor findByUsername(String username) {
        return donorDAO.findByUsername(username);
    }

    public boolean usernameExists(String username) {
        return donorDAO.findByUsername(username) != null;
    }

    public boolean emailExists(String email) {
        return donorDAO.findByEmail(email) != null;
    }
}