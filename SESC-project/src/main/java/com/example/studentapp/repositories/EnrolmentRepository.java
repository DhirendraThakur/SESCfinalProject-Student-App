package com.example.studentapp.repositories;

import com.example.studentapp.entities.Enrolment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrolmentRepository extends MongoRepository<Enrolment, String> {
}
