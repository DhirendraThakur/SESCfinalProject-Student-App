package com.library.libraryservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "book_loans")
public class BookLoan {
    @Id
    private String id;
    private String studentId;
    private String bookId;
    private String bookTitle;
    private String bookAuthor;
    private LocalDateTime borrowedAt;
    private LocalDateTime dueDate;
    private LocalDateTime returnedAt;
    private String status;
    private boolean fineIssued;
}
