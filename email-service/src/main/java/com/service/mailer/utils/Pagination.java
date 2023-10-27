package com.service.mailer.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class Pagination {
    public static Pageable createPageable(Map<String, String> queryParameters) {

        Integer page = queryParameters.containsKey("page") ? Integer.parseInt(queryParameters.get("page")) : null;
        Integer pageSize = queryParameters.containsKey("pageSize") ? Integer.parseInt(queryParameters.get("pageSize"))
                : null;
        String sortField = queryParameters.getOrDefault("sortField", null);
        Sort.Direction sortDirection = queryParameters.containsKey("sortDirection")
                ? Sort.Direction.valueOf(queryParameters.get("sortDirection").toUpperCase())
                : null;

        int defaultPage = 0;
        int defaultPageSize = 10;
        String defaultSortField = "id";
        Sort.Direction defaultSortDirection = Sort.Direction.ASC;

        int pageNum = page != null ? page : defaultPage;
        int size = pageSize != null ? pageSize : defaultPageSize;
        String field = sortField != null ? sortField : defaultSortField;
        Sort.Direction direction = sortDirection != null ? sortDirection : defaultSortDirection;

        return PageRequest.of(pageNum, size, Sort.by(direction, field));
    }

}
