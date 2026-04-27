package com.example.studentapp.controllers;

import com.example.studentapp.entities.*;
import com.example.studentapp.repositories.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*") // ✅ FIXED CORS

public class AdminController {

    private final Student_Repositories studentRepo;

    public AdminController(Student_Repositories studentRepo) {
        this.studentRepo = studentRepo;
    }

    // ================= STUDENTS =================

    @GetMapping("/students")
    public List<StudentEntities> getStudents() {
        return studentRepo.findAll();
    }

    @PutMapping("/students/{id}") // ✅ ADD UPDATE
    public StudentEntities updateStudent(@PathVariable String id,
                                         @RequestBody StudentEntities updated) {

        StudentEntities student = studentRepo.findById(id).orElseThrow();

        student.setName(updated.getName());
        student.setEmail(updated.getEmail());
        student.setPhone(updated.getPhone());
        student.setAddress(updated.getAddress());

        return studentRepo.save(student);
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable String id) {
        studentRepo.deleteById(id);
    }

}