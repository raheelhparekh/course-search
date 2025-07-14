package com.raheel.course_search.dto;

import com.raheel.course_search.document.CourseDocument;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CourseSearchResponse {
    private long total;
    private List<CourseDocument> courses;
}