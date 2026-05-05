package com.library.libraryservice.service;

import com.library.libraryservice.entities.Book;
import com.library.libraryservice.entities.BookLoan;
import com.library.libraryservice.repositories.BookLoanRepository;
import com.library.libraryservice.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookLoanServiceTest {

    @Mock
    private BookLoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BookLoanService bookLoanService;

    private Book sampleBook;
    private String studentId = "student123";

    @BeforeEach
    void setUp() {
        sampleBook = new Book();
        sampleBook.setId("book1");
        sampleBook.setTitle("Test Book");
        sampleBook.setAuthor("Test Author");
        sampleBook.setAvailableCopies(1);
    }

    @Test
    void testBorrowBook_Success() {
        when(bookRepository.findById("book1")).thenReturn(Optional.of(sampleBook));
        when(loanRepository.save(any(BookLoan.class))).thenAnswer(i -> i.getArguments()[0]);

        BookLoan loan = bookLoanService.borrowBook("book1", studentId);

        assertNotNull(loan);
        assertEquals(studentId, loan.getStudentId());
        assertEquals("BORROWED", loan.getStatus());
        assertEquals(0, sampleBook.getAvailableCopies());
        verify(bookRepository).save(sampleBook);
    }

    @Test
    void testBorrowBook_NoCopies() {
        sampleBook.setAvailableCopies(0);
        when(bookRepository.findById("book1")).thenReturn(Optional.of(sampleBook));

        assertThrows(RuntimeException.class, () -> bookLoanService.borrowBook("book1", studentId));
    }

    @Test
    void testReturnBook_Success() {
        BookLoan loan = new BookLoan();
        loan.setId("loan1");
        loan.setBookId("book1");
        loan.setStatus("BORROWED");
        loan.setDueDate(LocalDateTime.now().plusDays(1));

        when(loanRepository.findById("loan1")).thenReturn(Optional.of(loan));
        when(bookRepository.findById("book1")).thenReturn(Optional.of(sampleBook));
        when(loanRepository.save(any(BookLoan.class))).thenReturn(loan);

        BookLoan returnedLoan = bookLoanService.returnBook("loan1");

        assertEquals("RETURNED", returnedLoan.getStatus());
        assertEquals(2, sampleBook.getAvailableCopies());
        verify(bookRepository).save(sampleBook);
    }

    @Test
    void testReturnBook_LateFine() {
        BookLoan loan = new BookLoan();
        loan.setId("loan1");
        loan.setBookId("book1");
        loan.setStatus("BORROWED");
        loan.setDueDate(LocalDateTime.now().minusDays(1));
        loan.setStudentId(studentId);
        loan.setBookTitle("Test Book");

        when(loanRepository.findById("loan1")).thenReturn(Optional.of(loan));
        when(bookRepository.findById("book1")).thenReturn(Optional.of(sampleBook));
        when(loanRepository.save(any(BookLoan.class))).thenReturn(loan);

        ReflectionTestUtils.setField(bookLoanService, "financeServiceUrl", "http://finance-service");
        
        BookLoan returnedLoan = bookLoanService.returnBook("loan1");

        assertTrue(returnedLoan.isFineIssued());
        verify(restTemplate).postForObject(eq("http://finance-service/api/invoices"), any(Map.class), eq(String.class));
    }
}
