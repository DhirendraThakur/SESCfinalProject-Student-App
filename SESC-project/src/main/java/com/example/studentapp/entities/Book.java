package com.example.studentapp.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class Book {

    @Id
    private String id;
    private String title;
    private String author;
    private boolean available;
}