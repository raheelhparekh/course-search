package com.raheel.course_search.dto;

import lombok.Data;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.Instant;


@Data
public class SearchRequest {
    private String q;
    private Integer minAge;
    private Integer maxAge;
    private String category;
    private String type;
    private Double minPrice;
    private Double maxPrice;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant startDate;
    private String sort;
    private Integer page = 0;
    private Integer size = 10;
}