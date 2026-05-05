package com.example.studentapp.service;

import com.example.studentapp.entities.StudentEntities;
import com.example.studentapp.repositories.Student_Repositories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

class StudentServiceTest {

    @Mock
    private Student_Repositories repo;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(studentService, "libraryServiceUrl", "http://localhost:8081");
        ReflectionTestUtils.setField(studentService, "financeServiceUrl", "http://localhost:8082");
    }

    @Test
    void register_ShouldSaveStudent() {
        StudentEntities student = new StudentEntities();
        student.setId("1");
        student.setName("Anju");
        student.setEmail("anju@gmail.com");
        student.setPassword("1234");

        when(repo.findByEmail("anju@gmail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("encoded1234");
        when(repo.save(any(StudentEntities.class))).thenReturn(student);

        StudentEntities result = studentService.register(student);

        assertNotNull(result);
        assertEquals("Anju", result.getName());

        verify(repo, times(1)).save(any(StudentEntities.class));
    }

    @Test
    void register_ShouldFail_WhenEmailExists() {
        StudentEntities student = new StudentEntities();
        student.setEmail("anju@gmail.com");

        when(repo.findByEmail("anju@gmail.com")).thenReturn(Optional.of(student));

        assertThrows(RuntimeException.class, () -> {
            studentService.register(student);
        });

        verify(repo, never()).save(any(StudentEntities.class));
    }

    @Test
    void login_ShouldPass_WhenPasswordMatches() {
        StudentEntities student = new StudentEntities();
        student.setEmail("anju@gmail.com");
        student.setPassword("$2a$encodedPassword");

        when(repo.findByEmail("anju@gmail.com")).thenReturn(Optional.of(student));
        when(passwordEncoder.matches("1234", "$2a$encodedPassword")).thenReturn(true);

        Optional<StudentEntities> result = studentService.login("anju@gmail.com", "1234");

        assertTrue(result.isPresent());
        assertEquals("anju@gmail.com", result.get().getEmail());
    }

    @Test
    void login_ShouldFail_WhenPasswordWrong() {
        StudentEntities student = new StudentEntities();
        student.setEmail("anju@gmail.com");
        student.setPassword("$2a$encodedPassword");

        when(repo.findByEmail("anju@gmail.com")).thenReturn(Optional.of(student));
        when(passwordEncoder.matches("wrong", "$2a$encodedPassword")).thenReturn(false);

        Optional<StudentEntities> result = studentService.login("anju@gmail.com", "wrong");

        assertTrue(result.isEmpty());
    }

    @Test
    void delete_ShouldDeleteStudent() {
        studentService.delete("1");

        verify(repo, times(1)).deleteById("1");
    }
}