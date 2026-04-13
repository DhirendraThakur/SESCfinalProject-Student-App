package com.example.studentapp.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "enrolments")
public class Enrolment {

    @Id
    private String id;
    private String studentId;
    private String courseId;
    private LocalDateTime enrolledAt;
    private String status; // e.g., ACTIVE, COMPLETED, DROPPED
}
