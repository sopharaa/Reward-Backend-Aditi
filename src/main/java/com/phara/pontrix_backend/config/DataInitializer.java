package com.phara.pontrix_backend.config;

import com.phara.pontrix_backend.domain.Admin;
import com.phara.pontrix_backend.features.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (adminRepository.findByName("admin").isEmpty()) {

            Admin admin = new Admin();
            admin.setName("admin");
            admin.setPassword(passwordEncoder.encode("12345phara")); // ✅ HASHED
            adminRepository.save(admin);

            System.out.println("Default admin created:");
            System.out.println("Username: admin");
            System.out.println("Password: 123456");
        }
    }
}