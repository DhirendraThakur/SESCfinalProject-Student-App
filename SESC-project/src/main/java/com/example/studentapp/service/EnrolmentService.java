package com.example.studentapp.service;

import com.example.studentapp.dto.EnrolmentRequestDTO;
import com.example.studentapp.dto.EnrolmentResponseDTO;
import com.example.studentapp.entities.Course;
import com.example.studentapp.entities.Enrolment;
import com.example.studentapp.entities.StudentEntities;
import com.example.studentapp.repositories.CourseRepository;
import com.example.studentapp.repositories.EnrolmentRepository;
import com.example.studentapp.repositories.Student_Repositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EnrolmentService {

    private final EnrolmentRepository enrolmentRepository;
    private final CourseRepository courseRepository;
    private final Student_Repositories studentRepository;
    private final RestTemplate restTemplate;

    @Value("${finance.service.url}")
    private String financeServiceUrl;

    public EnrolmentService(EnrolmentRepository enrolmentRepository, 
                            CourseRepository courseRepository,
                            Student_Repositories studentRepository,
                            RestTemplate restTemplate) {
        this.enrolmentRepository = enrolmentRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.restTemplate = restTemplate;
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
        Enrolment savedEnrolment = enrolmentRepository.save(enrolment);

        // Create Finance invoice for course enrolment
        try {
            String financeUrl = financeServiceUrl + "/invoices/";
            Map<String, Object> invoiceBody = new HashMap<>();
            invoiceBody.put("studentId", savedEnrolment.getStudentId());
            invoiceBody.put("amount", 500.00);
            invoiceBody.put("type", "TUITION_FEES");
            invoiceBody.put("dueDate", LocalDate.now().plusDays(30).toString());
            restTemplate.postForObject(financeUrl, invoiceBody, String.class);
            System.out.println("Finance invoice created for: "
                    + savedEnrolment.getStudentId());
        } catch (Exception e) {
            System.err.println("Warning: Could not create invoice: "
                    + e.getMessage());
        }

        return savedEnrolment;
    }

    public List<EnrolmentResponseDTO> getStudentEnrolments(String studentId) {
        List<Enrolment> enrolments = enrolmentRepository.findByStudentId(studentId);
        List<EnrolmentResponseDTO> responses = new ArrayList<>();

        if (enrolments.isEmpty()) {
            return responses;
        }

        List<String> courseIds = enrolments.stream().map(Enrolment::getCourseId).collect(Collectors.toList());
        Iterable<Course> coursesSnapshot = courseRepository.findAllById(courseIds);
        
        Map<String, Course> courseMap = StreamSupport.stream(coursesSnapshot.spliterator(), false)
                .collect(Collectors.toMap(Course::getId, c -> c));

        // Resolve student name once to avoid redundant DB queries in the loop
        String studentName = studentRepository.findById(studentId)
                .map(s -> (s.getName() != null && !s.getName().trim().isEmpty()) ? s.getName() : s.getEmail())
                .orElse("Unknown Student");

        for (Enrolment e : enrolments) {
            Course course = courseMap.get(e.getCourseId());
            // If course is missing, we still want to show the enrolment but with a placeholder
            if (course == null) {
                course = new Course();
                course.setTitle("Unknown Course");
                course.setDescription("Course details not found");
            }

            responses.add(new EnrolmentResponseDTO(
                    e.getId(),
                    e.getStudentId(),
                    studentName,
                    course,
                    e.getEnrolledAt(),
                    e.getStatus()
            ));
        }
        return responses;
    }

    public List<EnrolmentResponseDTO> getAllEnrolments() {
        List<Enrolment> enrolments = enrolmentRepository.findAll();
        List<EnrolmentResponseDTO> responses = new ArrayList<>();
        
        if (enrolments.isEmpty()) return responses;

        // Bulk fetch courses and students for efficiency
        List<String> courseIds = enrolments.stream().map(Enrolment::getCourseId).distinct().collect(Collectors.toList());
        List<String> studentIds = enrolments.stream().map(Enrolment::getStudentId).distinct().collect(Collectors.toList());
        
        Iterable<Course> coursesSnapshot = courseRepository.findAllById(courseIds);
        Map<String, Course> courseMap = StreamSupport.stream(coursesSnapshot.spliterator(), false)
                .collect(Collectors.toMap(Course::getId, c -> c));
        
        Iterable<StudentEntities> studentsSnapshot = studentRepository.findAllById(studentIds);
        Map<String, String> studentMap = StreamSupport.stream(studentsSnapshot.spliterator(), false)
                .collect(Collectors.toMap(StudentEntities::getId, 
                    s -> (s.getName() != null && !s.getName().trim().isEmpty()) ? s.getName() : 
                         (s.getEmail() != null ? s.getEmail() : "No Name")));

        for (Enrolment e : enrolments) {
            Course course = courseMap.get(e.getCourseId());
            if (course == null) {
                course = new Course();
                course.setTitle("Unknown Course");
                course.setDescription("Course details not found");
            }

            responses.add(new EnrolmentResponseDTO(
                    e.getId(),
                    e.getStudentId(),
                    studentMap.getOrDefault(e.getStudentId(), "Unknown"),
                    course,
                    e.getEnrolledAt(),
                    e.getStatus()
            ));
        }
        return responses;
    }
}
