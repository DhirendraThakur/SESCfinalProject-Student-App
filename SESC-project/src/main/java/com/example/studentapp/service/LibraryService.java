package com.example.studentapp.service;

import com.example.studentapp.entities.Book;
import com.example.studentapp.entities.BorrowBook;
import com.example.studentapp.entities.StudentEntities;
import com.example.studentapp.repositories.BookRepository;
import com.example.studentapp.repositories.BorrowRepository;
import com.example.studentapp.repositories.Student_Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private BorrowRepository borrowRepo;

    @Autowired
    private Student_Repositories studentRepo;

    // ADD BOOK
    public Book addBook(Book book) {
        return bookRepo.save(book);
    }

    // GET ALL BOOKS
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    // DELETE BOOK
    public void deleteBook(String id) {
        bookRepo.deleteById(id);
    }

    // UPDATE BOOK
    public Book updateBook(String id, Book updated) {
        Book book = bookRepo.findById(id).orElseThrow();
        book.setTitle(updated.getTitle());
        book.setAuthor(updated.getAuthor());
        return bookRepo.save(book);
    }

    // ISSUE BOOK
    public BorrowBook issueBook(String studentId, String bookId) {

        StudentEntities student = studentRepo.findById(studentId).orElseThrow();
        Book book = bookRepo.findById(bookId).orElseThrow();

        if (!book.isAvailable()) {
            throw new RuntimeException("Book not available");
        }

        book.setAvailable(false);
        bookRepo.save(book);

        BorrowBook borrow = new BorrowBook();
        borrow.setStudentId(studentId);
        borrow.setStudentName(student.getName());
        borrow.setBookId(bookId);
        borrow.setBookTitle(book.getTitle());
        borrow.setIssueDate(LocalDate.now());

        return borrowRepo.save(borrow);
    }

    // RETURN BOOK + FINE
    public BorrowBook returnBook(String borrowId) {
        BorrowBook borrow = borrowRepo.findById(borrowId).orElseThrow();

        LocalDate today = LocalDate.now();
        borrow.setReturnDate(today);

        long days = java.time.temporal.ChronoUnit.DAYS.between(borrow.getIssueDate(), today);

        if (days > 7) {
            borrow.setFine((days - 7) * 2); // £2 per day
        }

        Book book = bookRepo.findById(borrow.getBookId()).orElseThrow();
        book.setAvailable(true);
        bookRepo.save(book);

        return borrowRepo.save(borrow);
    }

    public List<BorrowBook> getAllBorrowed() {
        return borrowRepo.findAll();
    }

}
