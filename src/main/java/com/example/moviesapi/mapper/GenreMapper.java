package com.example.moviesapi.mapper;

import com.example.moviesapi.dto.GenreDTO;
import com.example.moviesapi.entity.Genre;
import com.example.moviesapi.entity.Movie;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GenreMapper {

    public GenreDTO toDTO(Genre genre) {
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        if (genre.getMovies() != null) {
            dto.setMovies(genre.getMovies().stream()
                    .map(this::toMovieSummary)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    private GenreDTO.MovieSummaryDTO toMovieSummary(Movie movie) {
        GenreDTO.MovieSummaryDTO summary = new GenreDTO.MovieSummaryDTO();
        summary.setId(movie.getId());
        summary.setTitle(movie.getTitle());
        summary.setReleaseYear(movie.getReleaseYear());
        summary.setDuration(movie.getDuration());
        return summary;
    }
}