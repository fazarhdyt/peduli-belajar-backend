package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        user.getPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public UserDetails loadUserByUsername() {
        // Method implementation


    }

    public UserDetails loadUserByUsername(String username) {
    }
}