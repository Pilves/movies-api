package com.example.moviesapi.config;

import com.example.moviesapi.exception.InvalidPaginationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PaginationValidator {
    private final PaginationProperties properties;

    public PaginationValidator(PaginationProperties properties) {
        this.properties = properties;
    }

    public void validatePageable(Pageable pageable) {
        if (pageable.getPageNumber() < properties.getMinPageNumber() ||
                pageable.getPageSize() > properties.getMaxPageSize() ||
                pageable.getPageSize() < 1) {
            throw new InvalidPaginationException("Invalid pagination parameters");
        }
    }
}