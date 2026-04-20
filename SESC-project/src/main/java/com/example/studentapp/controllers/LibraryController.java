package com.example.studentapp.controllers;

import com.example.studentapp.entities.Book;
import com.example.studentapp.entities.BorrowBook;
import com.example.studentapp.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/library")
@CrossOrigin
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return libraryService.getAllBooks();
    }

    @GetMapping("/books/available")
    public List<Book> getAvailableBooks() {
        return libraryService.getAvailableBooks();
    }

    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestBody Map<String, String> request) {
        try {
            String bookId = request.get("bookId");
            String studentId = request.get("studentId");
            String studentName = request.get("studentName");
            BorrowBook borrowed = libraryService.borrowBook(bookId, studentId, studentName);
            return ResponseEntity.ok(borrowed);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return/{borrowId}")
    public ResponseEntity<?> returnBook(@PathVariable String borrowId) {
        try {
            BorrowBook returned = libraryService.returnBook(borrowId);
            return ResponseEntity.ok(returned);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history/{studentId}")
    public List<BorrowBook> getStudentBorrowHistory(@PathVariable String studentId) {
        return libraryService.getStudentBorrowHistory(studentId);
    }

    @GetMapping("/loans")
    public List<BorrowBook> getAllCurrentLoans() {
        return libraryService.getAllCurrentLoans();
    }
}
