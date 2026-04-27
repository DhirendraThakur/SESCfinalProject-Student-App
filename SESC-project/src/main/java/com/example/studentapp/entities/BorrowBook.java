package com.example.studentapp.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "borrow")
public class BorrowBook {

    @Id
    private String id;
    private String studentId;
    private String studentName;
    private String bookId;
    private String bookTitle;
    private LocalDate issueDate;
    private LocalDate returnDate;

    private double fine;
}