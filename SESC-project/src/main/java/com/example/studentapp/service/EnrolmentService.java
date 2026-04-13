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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrolmentService {

    private final EnrolmentRepository enrolmentRepository;
    private final CourseRepository courseRepository;

    public EnrolmentService(EnrolmentRepository enrolmentRepository, CourseRepository courseRepository) {
        this.enrolmentRepository = enrolmentRepository;
        this.courseRepository = courseRepository;
    }

    public Enrolment enrollStudent(EnrolmentRequestDTO request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + request.getCourseId()));

        boolean alreadyEnrolled = enrolmentRepository.findByStudentId(request.getStudentId()).stream()
                .anyMatch(existingEnrolment -> request.getCourseId().equals(existingEnrolment.getCourseId()));
        if (alreadyEnrolled) {
            throw new IllegalArgumentException(
                    "Student is already enrolled in course: " + request.getCourseId()
            );
        }

        Enrolment enrolment = new Enrolment();
        enrolment.setStudentId(request.getStudentId());
        enrolment.setCourseId(course.getId());
        enrolment.setEnrolledAt(LocalDateTime.now());
        enrolment.setStatus("ACTIVE");
        return enrolmentRepository.save(enrolment);
    }

    public List<EnrolmentResponseDTO> getStudentEnrolments(String studentId) {
        List<Enrolment> enrolments = enrolmentRepository.findByStudentId(studentId);
        List<EnrolmentResponseDTO> responses = new ArrayList<>();

        if (enrolments.isEmpty()) {
            return responses;
        }

        List<String> courseIds = enrolments.stream().map(Enrolment::getCourseId).collect(Collectors.toList());
        List<Course> courses = (List<Course>) courseRepository.findAllById(courseIds);
        
        Map<String, Course> courseMap = courses.stream()
                .collect(Collectors.toMap(Course::getId, c -> c));

        for (Enrolment e : enrolments) {
            Course course = courseMap.get(e.getCourseId());
            if (course == null) {
                throw new IllegalStateException(
                        "Enrolment " + e.getId() + " references missing course " + e.getCourseId()
                );
            }
            responses.add(new EnrolmentResponseDTO(
                    e.getId(),
                    e.getStudentId(),
                    course,
                    e.getEnrolledAt(),
                    e.getStatus()
            ));
        }
        return responses;
    }
}
