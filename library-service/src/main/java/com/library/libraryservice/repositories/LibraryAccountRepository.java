package com.library.libraryservice.repositories;

import com.library.libraryservice.entities.LibraryAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryAccountRepository extends MongoRepository<LibraryAccount, String> {
    Optional<LibraryAccount> findByStudentId(String studentId);
}
