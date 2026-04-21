package com.example.studentapp.service;

import com.example.studentapp.entities.StudentEntities;
import com.example.studentapp.repositories.Student_Repositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentService {

    private final Student_Repositories repo;
    private final RestTemplate restTemplate;

    @Value("${library.service.url}")
    private String libraryServiceUrl;

    @Value("${finance.service.url}")
    private String financeServiceUrl;

    public StudentService(Student_Repositories repo, RestTemplate restTemplate) {
        this.repo = repo;
        this.restTemplate = restTemplate;
    }

    public StudentEntities save(StudentEntities student) {

        return repo.save(student);
    }
    public StudentEntities register(StudentEntities student) {
        StudentEntities saved = repo.save(student);

        // Create Library account
        try {
            String libraryUrl = libraryServiceUrl + "/api/library/accounts/register";
            Map<String, String> libraryBody = new HashMap<>();
            libraryBody.put("studentId", saved.getId());
            restTemplate.postForObject(libraryUrl, libraryBody, String.class);
            System.out.println("Library account created for student: "
                    + saved.getId());
        } catch (Exception e) {
            System.err.println("Warning: Could not create library " +
                    "account for student " + saved.getId() +
                    ": " + e.getMessage());
        }

        // Create Finance account
        try {
            String financeUrl = financeServiceUrl + "/accounts/";
            Map<String, String> financeBody = new HashMap<>();
            financeBody.put("studentId", saved.getId());
            restTemplate.postForObject(financeUrl, financeBody, String.class);
            System.out.println("Finance account created for student: "
                    + saved.getId());
        } catch (Exception e) {
            System.err.println("Warning: Could not create finance " +
                    "account for student " + saved.getId() +
                    ": " + e.getMessage());
        }

        return saved;
    }
    public Optional<StudentEntities> getById(String id) {
        return repo.findById(id);
    }

//    public List<StudentEntities> getAll() {
//        return repo.findAll();
//    }
    public Optional<StudentEntities> login(String email, String password) {
        Optional<StudentEntities> user = repo.findByEmail(email);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }
    public StudentEntities update(String id, StudentEntities updated) {
        return repo.findById(id).map(student -> {
            student.setName(updated.getName());
            student.setEmail(updated.getEmail());
            student.setPhone(updated.getPhone());
            student.setAddress(updated.getAddress());
            return repo.save(student);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}