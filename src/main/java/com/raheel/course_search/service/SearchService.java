package com.raheel.course_search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.raheel.course_search.document.CourseDocument;
import com.raheel.course_search.dto.CourseSearchResponse;
import com.raheel.course_search.dto.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchClient client;

    public CourseSearchResponse searchCourses(SearchRequest request) throws Exception {
        List<Query> filters = new ArrayList<>();

        // Full-text search
        Query fullTextQuery;
        if (request.getQ() != null && !request.getQ().isBlank()) {
            fullTextQuery = MultiMatchQuery.of(m -> m
                    .fields("title", "description")
                    .query(request.getQ())
                    .fuzziness("AUTO")
            )._toQuery();
        } else {
            fullTextQuery = null;
        }

        // Category filter
        if (request.getCategory() != null) {
            filters.add(TermQuery.of(t -> t
                    .field("category.keyword")
                    .value(request.getCategory())
            )._toQuery());
        }

        // Type filter
        if (request.getType() != null) {
            filters.add(TermQuery.of(t -> t
                    .field("type.keyword")
                    .value(request.getType())
            )._toQuery());
        }

        // Age filter
        if (request.getMinAge() != null || request.getMaxAge() != null) {
            filters.add(RangeQuery.of(r -> r
                    .field("minAge")
                    .gte(request.getMinAge() != null ? JsonData.of(request.getMinAge()) : null)
                    .lte(request.getMaxAge() != null ? JsonData.of(request.getMaxAge()) : null)
            )._toQuery());
        }

        // Price filter
        if (request.getMinPrice() != null || request.getMaxPrice() != null) {
            filters.add(RangeQuery.of(r -> r
                    .field("price")
                    .gte(request.getMinPrice() != null ? JsonData.of(request.getMinPrice()) : null)
                    .lte(request.getMaxPrice() != null ? JsonData.of(request.getMaxPrice()) : null)
            )._toQuery());
        }

        // Start date filter
        if (request.getStartDate() != null) {
            long timestampMillis = request.getStartDate().toEpochMilli(); // ✅ convert Instant to long
            filters.add(RangeQuery.of(r -> r
                    .field("nextSessionDate")
                    .gte(JsonData.of(timestampMillis)) // ✅ send number
            )._toQuery());
        }

        // Combine all filters
        Query finalQuery = fullTextQuery != null
                ? BoolQuery.of(b -> b.must(fullTextQuery).filter(filters))._toQuery()
                : BoolQuery.of(b -> b.filter(filters))._toQuery();

        // Sorting (final values for lambdas)
        final String sortFieldFinal;
        final SortOrder sortOrderFinal;

        if ("priceAsc".equals(request.getSort())) {
            sortFieldFinal = "price";
            sortOrderFinal = SortOrder.Asc;
        } else if ("priceDesc".equals(request.getSort())) {
            sortFieldFinal = "price";
            sortOrderFinal = SortOrder.Desc;
        } else {
            sortFieldFinal = "nextSessionDate";
            sortOrderFinal = SortOrder.Asc;
        }

        // Execute search
        SearchResponse<CourseDocument> response = client.search(s -> s
                        .index("courses")
                        .query(finalQuery)
                        .sort(so -> so.field(f -> f.field(sortFieldFinal).order(sortOrderFinal)))
                        .from(request.getPage() * request.getSize())
                        .size(request.getSize()),
                CourseDocument.class
        );

        List<CourseDocument> courses = response.hits().hits().stream()
                .map(hit -> hit.source())
                .toList();

        long total = response.hits().total() != null ? response.hits().total().value() : 0;

        return new CourseSearchResponse(total, courses);
    }
}