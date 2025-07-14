package com.raheel.course_search.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.Instant;
import java.time.LocalDateTime;

@Document(indexName = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseDocument {

    @Id
    private String id;
    private String title;
    private String description;
    private String category;
    private String type;
    private String gradeRange;
    private int minAge;
    private int maxAge;
    private double price;

    @Field(type = FieldType.Date)
    private Instant nextSessionDate;
}