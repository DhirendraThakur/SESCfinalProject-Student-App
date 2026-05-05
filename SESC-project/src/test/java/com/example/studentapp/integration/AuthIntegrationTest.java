package com.example.studentapp.integration;

import com.example.studentapp.entities.StudentEntities;
import com.example.studentapp.repositories.Student_Repositories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "library.service.url=http://localhost:8081",
        "finance.service.url=http://localhost:8082",
        "spring.data.mongodb.uri=mongodb://localhost:27017/student-test-db"
})
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private Student_Repositories studentRepo;

    @Test
    void studentLogin_ShouldReturnToken_WhenCredentialsAreCorrect() throws Exception {
        StudentEntities student = new StudentEntities();
        student.setId("student123");
        student.setName("Anju");
        student.setEmail("anju@gmail.com");
        student.setPassword(passwordEncoder.encode("1234"));
        student.setPhone("9999999999");
        student.setAddress("UK");
        student.setRole("STUDENT");

        when(studentRepo.findByEmail("anju@gmail.com"))
                .thenReturn(Optional.of(student));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "anju@gmail.com",
                                  "password": "1234"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.id").value("student123"))
                .andExpect(jsonPath("$.name").value("Anju"))
                .andExpect(jsonPath("$.email").value("anju@gmail.com"))
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    @Test
    void adminLogin_ShouldReturnAdminRole_WhenCredentialsAreCorrect() throws Exception {
        StudentEntities admin = new StudentEntities();
        admin.setId("admin123");
        admin.setName("Admin User");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setPhone("1111111111");
        admin.setAddress("Admin Office");
        admin.setRole("ADMIN");

        when(studentRepo.findByEmail("admin@gmail.com"))
                .thenReturn(Optional.of(admin));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "admin@gmail.com",
                                  "password": "admin123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.id").value("admin123"))
                .andExpect(jsonPath("$.email").value("admin@gmail.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenPasswordIsWrong() throws Exception {
        StudentEntities student = new StudentEntities();
        student.setId("student123");
        student.setName("Anju");
        student.setEmail("anju@gmail.com");
        student.setPassword(passwordEncoder.encode("1234"));
        student.setRole("STUDENT");

        when(studentRepo.findByEmail("anju@gmail.com"))
                .thenReturn(Optional.of(student));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "anju@gmail.com",
                                  "password": "wrongpassword"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenEmailDoesNotExist() throws Exception {
        when(studentRepo.findByEmail("missing@gmail.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "missing@gmail.com",
                                  "password": "1234"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }
}