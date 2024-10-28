package com.example.moviesapi.dto;

import lombok.Data;
import java.util.Set;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private Integer releaseYear;
    private Integer duration;
    private Set<GenreSummaryDTO> genres;
    private Set<ActorSummaryDTO> actors;

    @Data
    public static class GenreSummaryDTO {
        private Long id;
        private String name;
    }

    @Data
    public static class ActorSummaryDTO {
        private Long id;
        private String name;
        private String birthDate;
    }
}