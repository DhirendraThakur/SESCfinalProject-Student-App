package com.library.libraryservice.controllers;

import com.library.libraryservice.entities.LibraryAccount;
import com.library.libraryservice.service.LibraryAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/library/accounts")
@CrossOrigin
public class LibraryAccountController {

    private final LibraryAccountService service;

    public LibraryAccountController(LibraryAccountService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<LibraryAccount> register(@RequestBody Map<String, String> payload) {
        String studentId = payload.get("studentId");
        if (studentId == null || studentId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        LibraryAccount account = service.createAccount(studentId);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<LibraryAccount> getAccount(@PathVariable String studentId) {
        try {
            return ResponseEntity.ok(service.getAccount(studentId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
