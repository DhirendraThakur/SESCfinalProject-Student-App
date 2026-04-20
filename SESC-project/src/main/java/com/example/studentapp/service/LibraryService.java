package com.example.studentapp.service;

import com.example.studentapp.entities.Book;
import com.example.studentapp.entities.BorrowBook;
import com.example.studentapp.repositories.BookRepository;
import com.example.studentapp.repositories.BorrowRepository;
import com.example.studentapp.repositories.Student_Repositories;
import com.example.studentapp.entities.StudentEntities;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;
    private final Student_Repositories studentRepository;

    public LibraryService(BookRepository bookRepository, 
                          BorrowRepository borrowRepository,
                          Student_Repositories studentRepository) {
        this.bookRepository = bookRepository;
        this.borrowRepository = borrowRepository;
        this.studentRepository = studentRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailable(true);
    }

    public BorrowBook borrowBook(String bookId, String studentId, String studentName) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        // Resolve student name from database for accuracy
        String resolvedName = studentName;
        StudentEntities student = studentRepository.findById(studentId).orElse(null);
        if (student != null && student.getName() != null) {
            resolvedName = student.getName();
        }

        BorrowBook borrowBook = new BorrowBook();
        borrowBook.setStudentId(studentId);
        borrowBook.setStudentName(resolvedName);
        borrowBook.setBookId(book.getId());
        borrowBook.setBookTitle(book.getTitle());
        borrowBook.setBorrowedAt(LocalDateTime.now());
        borrowBook.setDueDate(LocalDateTime.now().plusDays(14));
        borrowBook.setReturnedAt(null);
        borrowBook.setStatus("BORROWED");

        return borrowRepository.save(borrowBook);
    }

    public BorrowBook returnBook(String borrowId) {
        BorrowBook borrowBook = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));

        if ("RETURNED".equals(borrowBook.getStatus())) {
            throw new RuntimeException("Book already returned");
        }

        borrowBook.setReturnedAt(LocalDateTime.now());
        borrowBook.setStatus("RETURNED");

        Book book = bookRepository.findById(borrowBook.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setAvailable(true);
        bookRepository.save(book);

        // TODO: Finance service integration - create invoice if returnedAt is after dueDate

        return borrowRepository.save(borrowBook);
    }

    public List<BorrowBook> getStudentBorrowHistory(String studentId) {
        return borrowRepository.findByStudentId(studentId);
    }

    public List<BorrowBook> getAllCurrentLoans() {
        return borrowRepository.findByStatus("BORROWED");
    }
}
