package com.example.moviesapi.dto;

import lombok.Data;
import java.util.Map;

@Data
public class MovieStatistics {
    private Map<Integer, Long> moviesByYear;
    private Map<String, Long> moviesByGenre;
    private Map<String, Long> actorMovieCounts;
    private Map<Integer, Double> averageDurationByYear;
    private Double averageDuration;
    private Integer totalMovies;
    private Integer totalActors;
    private Integer totalGenres;
}