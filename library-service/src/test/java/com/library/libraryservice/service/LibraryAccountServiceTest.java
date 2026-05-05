package com.library.libraryservice.service;

import com.library.libraryservice.entities.LibraryAccount;
import com.library.libraryservice.repositories.LibraryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryAccountServiceTest {

    @Mock
    private LibraryAccountRepository repository;

    @InjectMocks
    private LibraryAccountService libraryAccountService;

    private LibraryAccount sampleAccount;

    @BeforeEach
    void setUp() {
        sampleAccount = new LibraryAccount();
        sampleAccount.setId("acc1");
        sampleAccount.setStudentId("std1");
        sampleAccount.setPin("000000");
        sampleAccount.setFirstLogin(true);
        sampleAccount.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateAccount_Existing() {
        when(repository.findByStudentId("std1")).thenReturn(Optional.of(sampleAccount));

        LibraryAccount account = libraryAccountService.createAccount("std1");

        assertNotNull(account);
        assertEquals("std1", account.getStudentId());
        verify(repository, never()).save(any(LibraryAccount.class));
    }

    @Test
    void testCreateAccount_New() {
        when(repository.findByStudentId("std1")).thenReturn(Optional.empty());
        when(repository.save(any(LibraryAccount.class))).thenAnswer(i -> i.getArguments()[0]);

        LibraryAccount account = libraryAccountService.createAccount("std1");

        assertNotNull(account);
        assertEquals("std1", account.getStudentId());
        assertEquals("000000", account.getPin());
        assertTrue(account.isFirstLogin());
        verify(repository, times(1)).save(any(LibraryAccount.class));
    }

    @Test
    void testGetAccount_Found() {
        when(repository.findByStudentId("std1")).thenReturn(Optional.of(sampleAccount));

        LibraryAccount account = libraryAccountService.getAccount("std1");

        assertNotNull(account);
        assertEquals("std1", account.getStudentId());
    }

    @Test
    void testGetAccount_NotFound() {
        when(repository.findByStudentId("std1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> libraryAccountService.getAccount("std1"));
    }
}
