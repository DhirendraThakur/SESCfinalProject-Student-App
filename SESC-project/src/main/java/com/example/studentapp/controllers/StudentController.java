package com.example.studentapp.controllers;

import com.example.studentapp.entities.StudentEntities;
import com.example.studentapp.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {

        this.studentService = studentService;
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

//    @GetMapping
//    public List<StudentEntities> getAllStudents() {
//
//        return studentService.getAll();
//    }
}