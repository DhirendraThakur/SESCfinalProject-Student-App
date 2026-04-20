package com.example.studentapp.repositories;

import com.example.studentapp.entities.BorrowBook;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends MongoRepository<BorrowBook, String> {
    List<BorrowBook> findByStudentId(String studentId);
    List<BorrowBook> findByStatus(String status);
    Optional<BorrowBook> findByBookIdAndStatus(String bookId, String status);
}