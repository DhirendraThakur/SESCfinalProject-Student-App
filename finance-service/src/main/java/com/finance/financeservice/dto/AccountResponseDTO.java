package com.finance.financeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private String id;
    private String studentId;
    private List<String> invoiceIds;
    private LocalDateTime createdAt;
    private boolean hasOutstandingBalance;
}
