package com.example.studentapp.dto;

import com.example.studentapp.entities.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrolmentResponseDTO {
    private String enrolmentId;
    private String studentId;
    private String studentName;
    private Course course;
    private LocalDateTime enrolledAt;
    private String status;
}
