package com.finance.financeservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "invoices")
public class Invoice {
    @Id
    private String id;

    @Indexed(unique = true)
    private String reference;

    private String studentId;
    private Double amount;
    private LocalDate dueDate;
    private String type; // "LIBRARY_FINE" or "TUITION_FEES"
    private String status; // "OUTSTANDING", "PAID", "CANCELLED"
    private LocalDateTime createdAt;
}
