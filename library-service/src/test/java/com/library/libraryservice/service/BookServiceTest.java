package com.library.libraryservice.service;

import com.library.libraryservice.entities.Book;
import com.library.libraryservice.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new Book();
        sampleBook.setId("1");
        sampleBook.setTitle("Test Book");
        sampleBook.setAuthor("Test Author");
        sampleBook.setIsbn("1234567890");
        sampleBook.setCopies(5);
        sampleBook.setAvailableCopies(5);
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(sampleBook));
        List<Book> books = bookService.getAllBooks();
        assertEquals(1, books.size());
        assertEquals("Test Book", books.get(0).getTitle());
    }

    @Test
    void testGetAvailableBooks() {
        when(bookRepository.findByAvailableCopiesGreaterThan(0)).thenReturn(Arrays.asList(sampleBook));
        List<Book> books = bookService.getAvailableBooks();
        assertEquals(1, books.size());
    }

    @Test
    void testAddBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);
        Book savedBook = bookService.addBook(sampleBook);
        assertEquals(5, savedBook.getAvailableCopies());
        verify(bookRepository, times(1)).save(sampleBook);
    }

    @Test
    void testUpdateBook_Success() {
        when(bookRepository.findById("1")).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        Book updatedInfo = new Book();
        updatedInfo.setTitle("Updated Title");
        updatedInfo.setAuthor("Updated Author");
        updatedInfo.setIsbn("0987654321");
        updatedInfo.setCopies(10);
        updatedInfo.setAvailableCopies(10);

        Book updatedBook = bookService.updateBook("1", updatedInfo);

        assertEquals("Updated Title", sampleBook.getTitle());
        verify(bookRepository, times(1)).save(sampleBook);
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById("1")).thenReturn(Optional.empty());

        Book updatedInfo = new Book();
        assertThrows(RuntimeException.class, () -> bookService.updateBook("1", updatedInfo));
    }

    @Test
    void testDeleteBook_Success() {
        when(bookRepository.findById("1")).thenReturn(Optional.of(sampleBook));
        bookService.deleteBook("1");
        verify(bookRepository, times(1)).delete(sampleBook);
    }
}
