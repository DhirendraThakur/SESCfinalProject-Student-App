package com.example.studentapp.controllers;

import com.example.studentapp.entities.*;
import com.example.studentapp.repositories.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    private final Student_Repositories studentRepo;
    private final BookRepository bookRepo;
    private final BorrowRepository borrowRepo;

    public AdminController(Student_Repositories studentRepo,
                           BookRepository bookRepo,
                           BorrowRepository borrowRepo) {
        this.studentRepo = studentRepo;
        this.bookRepo = bookRepo;
        this.borrowRepo = borrowRepo;
    }

    // STUDENTS
    @GetMapping("/students")
    public List<StudentEntities> getStudents() {
        return studentRepo.findAll();
    }
    // UPDATE STUDENT
    @PutMapping("/students/{id}")
    public StudentEntities updateStudent(@PathVariable String id, @RequestBody StudentEntities updated) {
        return studentRepo.findById(id).map(s -> {
            s.setName(updated.getName());
            s.setEmail(updated.getEmail());
            s.setPhone(updated.getPhone());
            s.setAddress(updated.getAddress());
            return studentRepo.save(s);
        }).orElseThrow();
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable String id) {
        studentRepo.deleteById(id);
    }

    // BOOKS
    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        book.setAvailable(true);
        return bookRepo.save(book);
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookRepo.findAll();
    }

    @PutMapping("/books/{id}")
    public Book updateBook(@PathVariable String id, @RequestBody Book updated) {
        return bookRepo.findById(id).map(b -> {
            b.setTitle(updated.getTitle());
            b.setAuthor(updated.getAuthor());
            return bookRepo.save(b);
        }).orElseThrow();
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable String id) {
        bookRepo.deleteById(id);
    }

    // BORROW
    @PostMapping("/borrow")
    public BorrowBook borrowBook(@RequestBody BorrowBook borrow) {
        // mark book unavailable
        Book book = bookRepo.findById(borrow.getBookId()).orElseThrow();
        book.setAvailable(false);
        bookRepo.save(book);

        // set new fields
        borrow.setBorrowedAt(LocalDateTime.now());
        borrow.setDueDate(LocalDateTime.now().plusDays(14));
        borrow.setReturnedAt(null);
        borrow.setStatus("BORROWED");

        return borrowRepo.save(borrow);
    }

    @GetMapping("/borrow")
    public List<BorrowBook> getBorrowDetails() {
        return borrowRepo.findAll();
    }

    @GetMapping("/borrow/overdue")
    public List<BorrowBook> getOverdueLoans() {
        return borrowRepo.findAll().stream()
                .filter(b -> "BORROWED".equals(b.getStatus()))
                .filter(b -> b.getDueDate().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }
}