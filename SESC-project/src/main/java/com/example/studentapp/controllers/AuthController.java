package com.example.studentapp.controllers;

import com.example.studentapp.dto.AuthResponseDTO;
import com.example.studentapp.entities.StudentEntities;
import com.example.studentapp.repositories.Student_Repositories;
import com.example.studentapp.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final Student_Repositories studentRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(Student_Repositories studentRepo,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.studentRepo = studentRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody StudentEntities loginRequest) {

        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Email and password required"));
        }

        StudentEntities user = studentRepo.findByEmail(loginRequest.getEmail().trim())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        }

        if (user.getPassword() == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        }

        boolean passwordMatches;

        if (isBCrypt(user.getPassword())) {
            passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        } else {
            passwordMatches = user.getPassword().equals(loginRequest.getPassword());

            if (passwordMatches) {
                user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
                studentRepo.save(user);
            }
        }

        if (!passwordMatches) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        }

        String role = user.getRole();

        if (role == null || role.isBlank()) {
            role = "STUDENT";
            user.setRole(role);
            studentRepo.save(user);
        }

        role = role.toUpperCase();

        String token = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                role
        );

        return ResponseEntity.ok(new AuthResponseDTO(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                role
        ));
    }

    private boolean isBCrypt(String password) {
        return password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$");
    }
}