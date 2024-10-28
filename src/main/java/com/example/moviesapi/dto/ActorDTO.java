package com.example.moviesapi.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class ActorDTO {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private Set<MovieSummaryDTO> movies;

    @Data
    public static class MovieSummaryDTO {
        private Long id;
        private String title;
        private Integer releaseYear;
        private Integer duration;
        private Set<String> genreNames;
    }
}