package com.example.studentapp.controllers;

import com.example.studentapp.entities.*;
import com.example.studentapp.repositories.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    private final Student_Repositories studentRepo;

    public AdminController(Student_Repositories studentRepo) {
        this.studentRepo = studentRepo;
    }

    // STUDENTS
    @GetMapping("/students")
    public List<StudentEntities> getStudents() {
        return studentRepo.findAll();
    }
    // UPDATE STUDENT
    @PutMapping("/students/{id}")
    public StudentEntities updateStudent(@PathVariable String id, @RequestBody StudentEntities updated) {
        return studentRepo.findById(id).map(s -> {
            s.setName(updated.getName());
            s.setEmail(updated.getEmail());
            s.setPhone(updated.getPhone());
            s.setAddress(updated.getAddress());
            return studentRepo.save(s);
        }).orElseThrow();
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable String id) {
        studentRepo.deleteById(id);
    }


}