package com.raheel.course_search.repository;

import com.raheel.course_search.document.CourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CourseRepository extends ElasticsearchRepository<CourseDocument, String> {
}