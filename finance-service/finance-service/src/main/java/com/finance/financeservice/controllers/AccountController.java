package com.finance.financeservice.controllers;

import com.finance.financeservice.dto.AccountRequestDTO;
import com.finance.financeservice.dto.AccountResponseDTO;
import com.finance.financeservice.entities.Account;
import com.finance.financeservice.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@CrossOrigin
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createAccount(@RequestBody AccountRequestDTO request) {
        try {
            Account account = accountService.createAccount(request.getStudentId());
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getAccountByStudentId(@PathVariable String studentId) {
        try {
            AccountResponseDTO response = accountService.getAccountByStudentId(studentId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
