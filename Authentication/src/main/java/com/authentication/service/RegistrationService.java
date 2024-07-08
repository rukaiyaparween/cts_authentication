// RegistrationService.java
package com.authentication.service;

import com.authentication.dto.RegistrationRequest;
import com.authentication.dto.RegistrationResponse;
import com.authentication.entity.User;
import com.authentication.repository.UserRepository;
import com.authentication.utils.EmailValidate;
import com.authentication.utils.PasswordValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RegistrationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public RegistrationResponse register(RegistrationRequest request) {
        // Validate email format
        if (!EmailValidate.isValid(request.getEmail())) {
            return new RegistrationResponse("Invalid email format.");
        }

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new RegistrationResponse("Email already exists.");
        }

        // Validate password
        if (!PasswordValidate.isValid(request.getPassword())) {
            return new RegistrationResponse("Password must be at least 8 characters long and contain a mix of letters, numbers, and special characters.");
        }

        // Encrypt password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Create and save user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setVerified(false);
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setTokenExpirationTime(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());

        return new RegistrationResponse("Registration successful. Verification email sent.");
    }

    public String verifyToken(String token) {
        Optional<User> userOptional = userRepository.findByVerificationToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getTokenExpirationTime().isAfter(LocalDateTime.now())) {
                user.setVerified(true);
                user.setVerificationToken(null);
                user.setTokenExpirationTime(null);
                userRepository.save(user);
                return "Verification successful.";
            } else {
                return "Token expired.";
            }
        } else {
            return "Invalid token.";
        }
    }
}
