package com.library.libraryservice.repositories;

import com.library.libraryservice.entities.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByAvailableCopiesGreaterThan(int copies);
    Optional<Book> findByIsbn(String isbn);
}
