package com.example.moviesapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class ActorDTO {
    private Long id;

    @NotBlank(message = "Actor name cannot be empty")
    private String name;

    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private Set<MovieSummaryDTO> movies;

    @Data
    public static class MovieSummaryDTO {
        private Long id;

        @NotBlank(message = "Movie title cannot be empty")
        private String title;

        @NotNull(message = "Release year cannot be null")
        private Integer releaseYear;

        @NotNull(message = "Duration cannot be null")
        private Integer duration;

        private Set<String> genreNames;
    }

    public String getBirthDateAsString() {
        return birthDate != null ? birthDate.toString() : null;
    }
}