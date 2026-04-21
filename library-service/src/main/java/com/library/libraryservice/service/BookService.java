package com.library.libraryservice.service;

import com.library.libraryservice.entities.Book;
import com.library.libraryservice.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    public List<Book> getAvailableBooks() {
        return repository.findByAvailableCopiesGreaterThan(0);
    }

    public Book addBook(Book book) {
        book.setAvailableCopies(book.getCopies());
        return repository.save(book);
    }

    public Book updateBook(String id, Book updated) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setTitle(updated.getTitle());
        book.setAuthor(updated.getAuthor());
        book.setIsbn(updated.getIsbn());
        book.setCopies(updated.getCopies());
        book.setAvailableCopies(updated.getAvailableCopies());
        return repository.save(book);
    }

    public void deleteBook(String id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        repository.delete(book);
    }
}
