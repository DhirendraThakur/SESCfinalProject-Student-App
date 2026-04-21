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
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    private final StudentService studentService;
    private final RestTemplate restTemplate;

    @Value("${finance.service.url}")
    private String financeServiceUrl;

    public StudentController(StudentService studentService, RestTemplate restTemplate) {
        this.studentService = studentService;
        this.restTemplate = restTemplate;
    }
    //For register user
    @PostMapping("/register")
    public StudentEntities register(@RequestBody StudentEntities student) {
        return studentService.register(student);
    }

    // For login
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
    public ResponseEntity<?> checkGraduation(
            @PathVariable String id) {
        try {
            String financeUrl = financeServiceUrl + "/accounts/student/" + id;
            Map response = restTemplate.getForObject(financeUrl, Map.class);

            boolean hasOutstanding = (boolean) response.get("hasOutstandingBalance");

            Map<String, Object> result = new HashMap<>();
            result.put("studentId", id);
            result.put("hasOutstandingBalance", hasOutstanding);
            result.put("eligible", !hasOutstanding);
            result.put("message", hasOutstanding
                    ? "Not eligible - outstanding invoices exist"
                    : "Eligible to graduate");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body("Could not check graduation eligibility: " + e.getMessage());
        }
    }

//    @GetMapping
//    public List<StudentEntities> getAllStudents() {
//
//        return studentService.getAll();
//    }
}