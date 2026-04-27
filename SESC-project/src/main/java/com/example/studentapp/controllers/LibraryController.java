package com.example.studentapp.controllers;

import com.example.studentapp.entities.Book;
import com.example.studentapp.entities.BorrowBook;
import com.example.studentapp.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@CrossOrigin

public class LibraryController {
    @Autowired
    private LibraryService service;

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        return service.addBook(book);
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return service.getAllBooks();
    }

    @PutMapping("/books/{id}")
    public Book update(@PathVariable String id, @RequestBody Book b) {
        return service.updateBook(id, b);
    }

    @DeleteMapping("/books/{id}")
    public void delete(@PathVariable String id) {
        service.deleteBook(id);
    }

    @PostMapping("/issue")
    public BorrowBook issue(@RequestParam String studentId, @RequestParam String bookId) {
        return service.issueBook(studentId, bookId);
    }

    @PostMapping("/return/{id}")
    public BorrowBook returnBook(@PathVariable String id) {
        return service.returnBook(id);
    }

    @GetMapping("/borrowed")
    public List<BorrowBook> borrowed() {
        return service.getAllBorrowed();
    }
}
