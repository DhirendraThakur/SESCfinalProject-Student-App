package com.example.studentapp.repositories;

import com.example.studentapp.entities.BorrowBook;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BorrowRepository extends MongoRepository<BorrowBook, String> {
}