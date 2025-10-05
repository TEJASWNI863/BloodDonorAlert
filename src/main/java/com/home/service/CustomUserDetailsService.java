package com.home.service;

import com.home.DAO.DonorDAO;
import com.home.model.Donor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private DonorDAO donorDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Donor donor = donorDAO.findByUsername(username);
        
        if (donor == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return User.builder()
                .username(donor.getUsername())
                .password(donor.getPassword())
                .roles("DONOR")
                .disabled(!donor.getEnabled())
                .build();
    }
}