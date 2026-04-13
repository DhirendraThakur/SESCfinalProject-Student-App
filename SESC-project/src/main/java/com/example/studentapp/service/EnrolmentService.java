package com.example.studentapp.service;

import com.example.studentapp.dto.EnrolmentRequestDTO;
import com.example.studentapp.dto.EnrolmentResponseDTO;
import com.example.studentapp.entities.Course;
import com.example.studentapp.entities.Enrolment;
import com.example.studentapp.repositories.CourseRepository;
import com.example.studentapp.repositories.EnrolmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EnrolmentService {

    private final EnrolmentRepository enrolmentRepository;
    private final CourseRepository courseRepository;

    public EnrolmentService(EnrolmentRepository enrolmentRepository, CourseRepository courseRepository) {
        this.enrolmentRepository = enrolmentRepository;
        this.courseRepository = courseRepository;
    }

    public Enrolment enrollStudent(EnrolmentRequestDTO request) {
        // Optional: check if already enrolled to prevent duplicates
        // For simplicity, just enrolling:
        Enrolment enrolment = new Enrolment();
        enrolment.setStudentId(request.getStudentId());
        enrolment.setCourseId(request.getCourseId());
        enrolment.setEnrolledAt(LocalDateTime.now());
        enrolment.setStatus("ACTIVE");
        return enrolmentRepository.save(enrolment);
    }

    public List<EnrolmentResponseDTO> getStudentEnrolments(String studentId) {
        List<Enrolment> enrolments = enrolmentRepository.findByStudentId(studentId);
        List<EnrolmentResponseDTO> responses = new ArrayList<>();

        for (Enrolment e : enrolments) {
            Optional<Course> courseOpt = courseRepository.findById(e.getCourseId());
            courseOpt.ifPresent(course -> responses.add(new EnrolmentResponseDTO(
                    e.getId(),
                    e.getStudentId(),
                    course,
                    e.getEnrolledAt(),
                    e.getStatus()
            )));
        }
        return responses;
    }
}
