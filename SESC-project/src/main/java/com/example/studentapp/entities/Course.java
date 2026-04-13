package com.example.studentapp.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "courses")
public class Course {

    @Id
    private String id;
    private String title;
    private String description;
    private int credits;
    private int duration; // in weeks
}
