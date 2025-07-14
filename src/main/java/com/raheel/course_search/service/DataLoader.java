package com.raheel.course_search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raheel.course_search.document.CourseDocument;
import com.raheel.course_search.repository.CourseRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final CourseRepository courseRepository;

    @PostConstruct
    public void loadData() {
        try {
            // Load JSON file from resources
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            InputStream is = getClass().getResourceAsStream("/sample-courses.json");

            // Convert JSON to List<CourseDocument>
            List<CourseDocument> courses = mapper.readValue(is, new TypeReference<List<CourseDocument>>() {});

            // Save to Elasticsearch
            courseRepository.saveAll(courses);

            System.out.println("✅ Sample courses indexed successfully: " + courses.size());
        } catch (Exception e) {
            System.err.println("❌ Failed to load sample data: " + e.getMessage());
        }
    }
}