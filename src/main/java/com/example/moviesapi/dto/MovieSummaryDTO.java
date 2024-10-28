package com.example.moviesapi.dto;

import lombok.Data;

@Data
public class MovieSummaryDTO {
    private Long id;
    private String title;
    private Integer releaseYear;
    private Integer duration;
}