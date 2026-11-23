package com.navya.employeemanagement.config;

import com.navya.employeemanagement.entity.User;
import com.navya.employeemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    public AdminBootstrapRunner(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (adminEmail.isBlank() && adminPassword.isBlank()) {
            return;
        }

        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            throw new IllegalStateException(
                    "Both ADMIN_EMAIL and ADMIN_PASSWORD must be configured"
            );
        }

        if (userRepository.existsByEmail(adminEmail)) {
            System.out.println("Admin account already exists.");
            return;
        }

        String encodedPassword =
                passwordEncoder.encode(adminPassword);

        User admin = new User(
                adminEmail,
                encodedPassword,
                "ADMIN"
        );

        userRepository.save(admin);

        System.out.println("Initial admin account created.");
    }
}
