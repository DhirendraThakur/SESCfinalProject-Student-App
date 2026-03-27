package com.example.studentapp.repositories;

import com.example.studentapp.entities.StudentEntities;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface Student_Repositories extends MongoRepository< StudentEntities , String> {
    Optional<StudentEntities> findByEmail(String email);
}