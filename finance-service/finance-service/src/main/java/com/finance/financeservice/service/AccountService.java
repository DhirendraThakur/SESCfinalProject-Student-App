package com.finance.financeservice.service;

import com.finance.financeservice.dto.AccountResponseDTO;
import com.finance.financeservice.entities.Account;
import com.finance.financeservice.entities.Invoice;
import com.finance.financeservice.repositories.AccountRepository;
import com.finance.financeservice.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final InvoiceRepository invoiceRepository;

    public AccountService(AccountRepository accountRepository, InvoiceRepository invoiceRepository) {
        this.accountRepository = accountRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public Account createAccount(String studentId) {
        Optional<Account> existing = accountRepository.findByStudentId(studentId);
        if (existing.isPresent()) {
            return existing.get();
        }

        Account newAccount = new Account();
        newAccount.setStudentId(studentId);
        newAccount.setInvoiceIds(new ArrayList<>());
        newAccount.setCreatedAt(LocalDateTime.now());
        return accountRepository.save(newAccount);
    }

    public AccountResponseDTO getAccountByStudentId(String studentId) {
        Account account = accountRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<Invoice> outstandingInvoices = invoiceRepository.findByStudentIdAndStatus(studentId, "OUTSTANDING");
        boolean hasOutstandingBalance = !outstandingInvoices.isEmpty();

        return new AccountResponseDTO(
                account.getId(),
                account.getStudentId(),
                account.getInvoiceIds(),
                account.getCreatedAt(),
                hasOutstandingBalance
        );
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
