package com.example.studentapp.service;

import com.example.studentapp.entities.StudentEntities;
import com.example.studentapp.repositories.Student_Repositories;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final Student_Repositories repo;

    public StudentService(Student_Repositories repo) {

        this.repo = repo;
    }

    public StudentEntities save(StudentEntities student) {

        return repo.save(student);
    }
    public StudentEntities register(StudentEntities student) {
        return repo.save(student);
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
}