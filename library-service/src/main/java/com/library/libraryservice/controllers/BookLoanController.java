package com.library.libraryservice.controllers;

import com.library.libraryservice.entities.BookLoan;
import com.library.libraryservice.service.BookLoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/library/loans")
@CrossOrigin
public class BookLoanController {

    private final BookLoanService service;

    public BookLoanController(BookLoanService service) {
        this.service = service;
    }

    @PostMapping("/borrow")
    public ResponseEntity<BookLoan> borrowBook(@RequestBody Map<String, String> payload) {
        try {
            String bookId = payload.get("bookId");
            String studentId = payload.get("studentId");
            return ResponseEntity.ok(service.borrowBook(bookId, studentId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/return/{loanId}")
    public ResponseEntity<BookLoan> returnBook(@PathVariable String loanId) {
        try {
            return ResponseEntity.ok(service.returnBook(loanId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/student/{studentId}")
    public List<BookLoan> getStudentLoans(@PathVariable String studentId) {
        return service.getStudentLoans(studentId);
    }

    @GetMapping("/current")
    public List<BookLoan> getAllCurrentLoans() {
        return service.getAllCurrentLoans();
    }

    @GetMapping("/overdue")
    public List<BookLoan> getOverdueLoans() {
        return service.getOverdueLoans();
    }
}
