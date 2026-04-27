package com.library.libraryservice.service;

import com.library.libraryservice.entities.LibraryAccount;
import com.library.libraryservice.repositories.LibraryAccountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LibraryAccountService {

    private final LibraryAccountRepository repository;

    public LibraryAccountService(LibraryAccountRepository repository) {
        this.repository = repository;
    }

    public LibraryAccount createAccount(String studentId) {
        return repository.findByStudentId(studentId).orElseGet(() -> {
            LibraryAccount newAccount = new LibraryAccount();
            newAccount.setStudentId(studentId);
            newAccount.setPin("000000");
            newAccount.setCreatedAt(LocalDateTime.now());
            newAccount.setFirstLogin(true);
            return repository.save(newAccount);
        });
    }

    public LibraryAccount getAccount(String studentId) {
        return repository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}
