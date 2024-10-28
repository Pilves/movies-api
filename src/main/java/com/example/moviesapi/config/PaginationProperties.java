package com.example.moviesapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.pagination")
@Getter
@Setter
public class PaginationProperties {
    private int maxPageSize = 50;
    private int defaultPageSize = 10;
    private int minPageNumber = 0;
}