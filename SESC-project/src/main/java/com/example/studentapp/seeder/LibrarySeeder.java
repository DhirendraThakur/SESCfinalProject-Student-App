package com.example.studentapp.seeder;

import com.example.studentapp.entities.Book;
import com.example.studentapp.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class LibrarySeeder implements CommandLineRunner {

    private final BookRepository bookRepository;

    public LibrarySeeder(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() == 0) {
            System.out.println("Seeding books into MongoDB...");
            bookRepository.saveAll(Arrays.asList(
                    new Book(null, "Clean Code", "Robert C. Martin", true),
                    new Book(null, "The Pragmatic Programmer", "Andrew Hunt", true),
                    new Book(null, "Design Patterns", "Gang of Four", true),
                    new Book(null, "Introduction to Algorithms", "Thomas H. Cormen", true),
                    new Book(null, "Computer Networks", "Andrew S. Tanenbaum", true),
                    new Book(null, "Artificial Intelligence: A Modern Approach", "Stuart Russell", true),
                    new Book(null, "Database System Concepts", "Abraham Silberschatz", true),
                    new Book(null, "Operating System Concepts", "Abraham Silberschatz", true),
                    new Book(null, "Software Engineering", "Ian Sommerville", true),
                    new Book(null, "Compilers: Principles, Techniques and Tools", "Alfred V. Aho", true)
            ));
        }
    }
}
