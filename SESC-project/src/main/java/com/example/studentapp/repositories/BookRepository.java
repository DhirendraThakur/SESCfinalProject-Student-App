package com.example.studentapp.repositories;

import com.example.studentapp.entities.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
}