package com.example.studentapp.controllers;

import com.example.studentapp.dto.EnrolmentRequestDTO;
import com.example.studentapp.dto.EnrolmentResponseDTO;
import com.example.studentapp.entities.Enrolment;
import com.example.studentapp.service.EnrolmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrolments")
@CrossOrigin
public class EnrolmentController {

    private final EnrolmentService enrolmentService;

    public EnrolmentController(EnrolmentService enrolmentService) {
        this.enrolmentService = enrolmentService;
    }

    @PostMapping
    public Enrolment enroll(@RequestBody EnrolmentRequestDTO request) {
        return enrolmentService.enrollStudent(request);
    }

    @GetMapping("/{studentId}")
    public List<EnrolmentResponseDTO> getStudentEnrolments(@PathVariable String studentId) {
        return enrolmentService.getStudentEnrolments(studentId);
    }
}
