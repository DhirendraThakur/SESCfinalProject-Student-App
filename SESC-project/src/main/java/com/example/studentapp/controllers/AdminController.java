package com.example.studentapp.controllers;

import com.example.studentapp.entities.*;
import com.example.studentapp.repositories.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*") // ✅ FIXED CORS

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

    // ================= STUDENTS =================

    @GetMapping("/students")
    public List<StudentEntities> getStudents() {
        return studentRepo.findAll();
    }

    @PutMapping("/students/{id}") // ✅ ADD UPDATE
    public StudentEntities updateStudent(@PathVariable String id,
                                         @RequestBody StudentEntities updated) {

        StudentEntities student = studentRepo.findById(id).orElseThrow();

        student.setName(updated.getName());
        student.setEmail(updated.getEmail());
        student.setPhone(updated.getPhone());
        student.setAddress(updated.getAddress());

        return studentRepo.save(student);
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable String id) {
        studentRepo.deleteById(id);
    }

    // ================= BOOKS =================

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        book.setAvailable(true); // ✅ ALWAYS AVAILABLE WHEN CREATED
        return bookRepo.save(book);
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookRepo.findAll();
    }

    @PutMapping("/books/{id}") // ✅ ADD UPDATE
    public Book updateBook(@PathVariable String id,
                           @RequestBody Book updated) {

        Book book = bookRepo.findById(id).orElseThrow();

        book.setTitle(updated.getTitle());
        book.setAuthor(updated.getAuthor());

        return bookRepo.save(book);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable String id) {
        bookRepo.deleteById(id);
    }

    // ================= ISSUE BOOK =================

    @PostMapping("/issue")
    public BorrowBook issueBook(@RequestParam String studentId,
                                @RequestParam String bookId) {

        StudentEntities student = studentRepo.findById(studentId).orElseThrow();
        Book book = bookRepo.findById(bookId).orElseThrow();

        if (!book.isAvailable()) {
            throw new RuntimeException("Book already issued");
        }

        book.setAvailable(false);
        bookRepo.save(book);

        BorrowBook borrow = new BorrowBook();
        borrow.setStudentId(student.getId());
        borrow.setStudentName(student.getName());
        borrow.setBookId(book.getId());
        borrow.setBookTitle(book.getTitle());

        return borrowRepo.save(borrow);
    }

    // ================= RETURN BOOK =================

    @PostMapping("/return/{id}") // ✅ NEW
    public BorrowBook returnBook(@PathVariable String id) {

        BorrowBook borrow = borrowRepo.findById(id).orElseThrow();

        Book book = bookRepo.findById(borrow.getBookId()).orElseThrow();
        book.setAvailable(true);
        bookRepo.save(book);

        borrow.setReturnDate(java.time.LocalDate.now());

        return borrowRepo.save(borrow);
    }

    // ================= BORROW LIST =================

    @GetMapping("/borrow")
    public List<BorrowBook> getBorrow() {
        return borrowRepo.findAll();
    }
}