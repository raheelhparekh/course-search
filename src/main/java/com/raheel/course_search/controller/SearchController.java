package com.raheel.course_search.controller;

import com.raheel.course_search.dto.SearchRequest;
import com.raheel.course_search.dto.CourseSearchResponse;
import com.raheel.course_search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public CourseSearchResponse search(@ModelAttribute SearchRequest request) throws Exception {
        return searchService.searchCourses(request);
    }

    //  Autocomplete endpoint
    @GetMapping("/suggest")
    public List<String> suggest(@RequestParam("q") String prefix) throws Exception {
        return searchService.suggestTitles(prefix);
    }
}