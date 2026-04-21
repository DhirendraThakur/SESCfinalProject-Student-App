package com.library.libraryservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "library_accounts")
public class LibraryAccount {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String studentId;
    
    private String pin;
    private LocalDateTime createdAt;
    private boolean firstLogin;
}
