package com.library.libraryservice.repositories;

import com.library.libraryservice.entities.BookLoan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookLoanRepository extends MongoRepository<BookLoan, String> {
    List<BookLoan> findByStudentId(String studentId);
    List<BookLoan> findByStatus(String status);
    Optional<BookLoan> findByBookIdAndStatus(String bookId, String status);
}
