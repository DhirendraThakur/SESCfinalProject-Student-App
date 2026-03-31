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
    public Object login(@RequestBody StudentEntities student) {
        Optional<StudentEntities> user = studentService.login(student.getEmail(), student.getPassword());

        if (user.isPresent()) {
            return user.get();
        } else {
            return "Invalid email or password";
        }
    }

    @PutMapping("/{id}")
    public StudentEntities updateStudent(@PathVariable String id, @RequestBody StudentEntities updatedStudent) {

        return studentService.getById(id).map(student -> {

            student.setName(updatedStudent.getName());
            student.setEmail(updatedStudent.getEmail());
            student.setPhoneno(updatedStudent.getPhoneno());
            student.setAddress(updatedStudent.getAddress());

            return studentService.save(student);

        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

//    @GetMapping
//    public List<StudentEntities> getAllStudents() {
//
//        return studentService.getAll();
//    }
}