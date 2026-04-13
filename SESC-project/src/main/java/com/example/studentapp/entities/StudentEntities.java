package com.example.studentapp.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "students")
public class StudentEntities {

        @Id
        private String id;
        private String name;
        private String email;
        private String course;
        private String password;
        private String address;
        private String phone;
}