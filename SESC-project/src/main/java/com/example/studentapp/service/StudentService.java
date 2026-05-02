package com.example.studentapp.service;

import com.example.studentapp.entities.StudentEntities;
import com.example.studentapp.repositories.Student_Repositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentService {

    private final Student_Repositories repo;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    @Value("${library.service.url}")
    private String libraryServiceUrl;

    @Value("${finance.service.url}")
    private String financeServiceUrl;

    public StudentService(Student_Repositories repo,
                          RestTemplate restTemplate,
                          PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public StudentEntities save(StudentEntities student) {
        return repo.save(student);
    }

    public StudentEntities register(StudentEntities student) {

        Optional<StudentEntities> existingUser = repo.findByEmail(student.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (student.getRole() == null || student.getRole().isBlank()) {
            student.setRole("STUDENT");
        }

        if (student.getPassword() != null && !student.getPassword().isBlank()) {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
        }

        StudentEntities saved = repo.save(student);

        try {
            String libraryUrl = libraryServiceUrl + "/api/library/accounts/register";

            Map<String, String> libraryBody = new HashMap<>();
            libraryBody.put("studentId", saved.getId());

            restTemplate.postForObject(libraryUrl, libraryBody, String.class);

            System.out.println("Library account created for student: " + saved.getId());

        } catch (Exception e) {
            System.err.println("Warning: Could not create library account for student "
                    + saved.getId() + ": " + e.getMessage());
        }

        try {
            String financeUrl = financeServiceUrl + "/accounts/";

            Map<String, String> financeBody = new HashMap<>();
            financeBody.put("studentId", saved.getId());

            restTemplate.postForObject(financeUrl, financeBody, String.class);

            System.out.println("Finance account created for student: " + saved.getId());

        } catch (Exception e) {
            System.err.println("Warning: Could not create finance account for student "
                    + saved.getId() + ": " + e.getMessage());
        }

        return saved;
    }

    public Optional<StudentEntities> getById(String id) {
        return repo.findById(id);
    }

    public Optional<StudentEntities> login(String email, String password) {
        Optional<StudentEntities> user = repo.findByEmail(email);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        StudentEntities foundUser = user.get();

        if (foundUser.getPassword() == null) {
            return Optional.empty();
        }

        boolean passwordMatched;

        if (foundUser.getPassword().startsWith("$2a$")
                || foundUser.getPassword().startsWith("$2b$")
                || foundUser.getPassword().startsWith("$2y$")) {

            passwordMatched = passwordEncoder.matches(password, foundUser.getPassword());

        } else {
            passwordMatched = foundUser.getPassword().equals(password);

            if (passwordMatched) {
                foundUser.setPassword(passwordEncoder.encode(password));
                repo.save(foundUser);
            }
        }

        if (passwordMatched) {
            return Optional.of(foundUser);
        }

        return Optional.empty();
    }

    public StudentEntities update(String id, StudentEntities updated) {
        return repo.findById(id).map(student -> {

            student.setName(updated.getName());
            student.setEmail(updated.getEmail());
            student.setPhone(updated.getPhone());
            student.setAddress(updated.getAddress());

            if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
                student.setPassword(passwordEncoder.encode(updated.getPassword()));
            }

            if (updated.getRole() != null && !updated.getRole().isBlank()) {
                student.setRole(updated.getRole());
            }

            return repo.save(student);

        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}