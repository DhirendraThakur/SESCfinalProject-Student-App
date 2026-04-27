package com.library.libraryservice.seeder;

import com.library.libraryservice.entities.Book;
import com.library.libraryservice.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;

    public BookSeeder(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() == 0) {
            List<Book> books = List.of(
                    new Book(null, "9780132350884", "Clean Code", "Robert C. Martin", 3, 3),
                    new Book(null, "9780201616224", "The Pragmatic Programmer", "Andrew Hunt", 2, 2),
                    new Book(null, "9780596517748", "JavaScript: The Good Parts", "Douglas Crockford", 2, 2),
                    new Book(null, "9780132350885", "Design Patterns", "Gang of Four", 3, 3),
                    new Book(null, "9780262033848", "Introduction to Algorithms", "Thomas H. Cormen", 2, 2),
                    new Book(null, "9780133970777", "Computer Networks", "Andrew S. Tanenbaum", 2, 2),
                    new Book(null, "9780135957059", "The Pragmatic Programmer 2nd Ed", "David Thomas", 2, 2),
                    new Book(null, "9780201633610", "Design Patterns: GoF", "Erich Gamma", 3, 3),
                    new Book(null, "9780596009205", "Head First Design Patterns", "Eric Freeman", 2, 2),
                    new Book(null, "9781491950357", "Learning Java", "Patrick Niemeyer", 2, 2)
            );
            bookRepository.saveAll(books);
            System.out.println("Books seeded successfully!");
        }
    }
}
