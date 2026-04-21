package com.library.libraryservice.service;

import com.library.libraryservice.entities.Book;
import com.library.libraryservice.entities.BookLoan;
import com.library.libraryservice.repositories.BookLoanRepository;
import com.library.libraryservice.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookLoanService {

    private final BookLoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    
    @Value("${finance.service.url}")
    private String financeServiceUrl;

    public BookLoanService(BookLoanRepository loanRepository, BookRepository bookRepository, RestTemplate restTemplate) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
    }

    public BookLoan borrowBook(String bookId, String studentId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No copies available");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BookLoan loan = new BookLoan();
        loan.setStudentId(studentId);
        loan.setBookId(book.getId());
        loan.setBookTitle(book.getTitle());
        loan.setBookAuthor(book.getAuthor());
        loan.setBorrowedAt(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusDays(14));
        loan.setReturnedAt(null);
        loan.setStatus("BORROWED");
        loan.setFineIssued(false);

        return loanRepository.save(loan);
    }

    public BookLoan returnBook(String loanId) {
        BookLoan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if ("RETURNED".equals(loan.getStatus())) {
            throw new RuntimeException("Book already returned");
        }

        loan.setReturnedAt(LocalDateTime.now());
        loan.setStatus("RETURNED");

        Book book = bookRepository.findById(loan.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        if (loan.getReturnedAt().isAfter(loan.getDueDate()) && !loan.isFineIssued()) {
            try {
                Map<String, Object> invoiceReq = new HashMap<>();
                invoiceReq.put("studentId", loan.getStudentId());
                invoiceReq.put("description", "Late return fine for: " + loan.getBookTitle());
                invoiceReq.put("amount", 5.00);

                restTemplate.postForObject(financeServiceUrl + "/api/invoices", invoiceReq, String.class);
                loan.setFineIssued(true);
                System.out.println("Fine issued for student: " + loan.getStudentId());
            } catch (Exception e) {
                System.out.println("Warning: Could not issue fine: " + e.getMessage());
            }
        }

        return loanRepository.save(loan);
    }

    public List<BookLoan> getStudentLoans(String studentId) {
        return loanRepository.findByStudentId(studentId);
    }

    public List<BookLoan> getAllCurrentLoans() {
        return loanRepository.findByStatus("BORROWED");
    }

    public List<BookLoan> getOverdueLoans() {
        return loanRepository.findByStatus("BORROWED").stream()
                .filter(loan -> loan.getDueDate() != null && loan.getDueDate().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }
}
