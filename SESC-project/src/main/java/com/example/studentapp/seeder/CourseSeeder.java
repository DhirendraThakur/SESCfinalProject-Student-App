package com.example.studentapp.seeder;

import com.example.studentapp.entities.Course;
import com.example.studentapp.repositories.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CourseSeeder implements CommandLineRunner {

    private final CourseRepository courseRepository;

    public CourseSeeder(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (courseRepository.count() == 0) {
            System.out.println("Seeding courses into MongoDB...");
            Course course1 = new Course(null, "Introduction to Algorithms", "Learn the basics of computer algorithms", 3, 12);
            Course course2 = new Course(null, "Web Development", "Learn HTML, CSS, JS and Spring Boot", 4, 16);
            Course course3 = new Course(null, "Database Management", "SQL and NoSQL database fundamentals", 3, 12);
            courseRepository.saveAll(Arrays.asList(course1, course2, course3));
        }
    }
}
