package com.example.studentapp.controllers;

import com.example.studentapp.entities.StudentEntities;
import com.example.studentapp.service.StudentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    private final StudentService studentService;
    private final RestTemplate restTemplate;

    @Value("${finance.service.url}")
    private String financeServiceUrl;

    @Value("${library.service.url}")
    private String libraryServiceUrl;

    public StudentController(StudentService studentService, RestTemplate restTemplate) {
        this.studentService = studentService;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/register")
    public StudentEntities register(@RequestBody StudentEntities student) {
        return studentService.register(student);
    }

    @PostMapping("/login")
    public StudentEntities login(@RequestBody StudentEntities student) {
        return studentService.login(student.getEmail(), student.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    @PutMapping("/{id}")
    public StudentEntities update(@PathVariable String id, @RequestBody StudentEntities student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        studentService.delete(id);
    }

    @GetMapping("/{id}/graduation")
    public ResponseEntity<?> checkGraduation(@PathVariable String id) {
        Map<String, Object> result = new HashMap<>();

        boolean hasOutstandingBalance = false;
        boolean hasBorrowedBooks = false;

        try {
            String financeUrl = financeServiceUrl + "/accounts/student/" + id;
            Map financeResponse = restTemplate.getForObject(financeUrl, Map.class);

            if (financeResponse != null && financeResponse.get("hasOutstandingBalance") != null) {
                hasOutstandingBalance = Boolean.TRUE.equals(financeResponse.get("hasOutstandingBalance"));
            }
        } catch (Exception e) {
            result.put("financeWarning", "Finance check failed: " + e.getMessage());
            hasOutstandingBalance = true;
        }

        try {
            String libraryUrl = libraryServiceUrl + "/api/library/loans/student/" + id;
            List<Map<String, Object>> loans = restTemplate.getForObject(libraryUrl, List.class);

            if (loans != null) {
                hasBorrowedBooks = loans.stream()
                        .anyMatch(loan -> "BORROWED".equals(String.valueOf(loan.get("status"))));
            }
        } catch (Exception e) {
            result.put("libraryWarning", "Library check failed: " + e.getMessage());
            hasBorrowedBooks = true;
        }

        boolean eligible = !hasOutstandingBalance && !hasBorrowedBooks;

        result.put("studentId", id);
        result.put("hasOutstandingBalance", hasOutstandingBalance);
        result.put("hasBorrowedBooks", hasBorrowedBooks);
        result.put("eligible", eligible);

        if (eligible) {
            result.put("message", "Eligible to graduate");
        } else if (hasOutstandingBalance && hasBorrowedBooks) {
            result.put("message", "Not eligible - pending payment and borrowed books exist");
        } else if (hasOutstandingBalance) {
            result.put("message", "Not eligible - pending payment exists");
        } else {
            result.put("message", "Not eligible - borrowed books must be returned");
        }

        return ResponseEntity.ok(result);
    }
}