package com.example.moviesapi.dto;

import lombok.Data;
import java.util.Set;

@Data
public class GenreDTO {
    private Long id;
    private String name;
    private Set<MovieSummaryDTO> movies;

    @Data
    public static class MovieSummaryDTO {
        private Long id;
        private String title;
        private Integer releaseYear;
        private Integer duration;
    }
}