package com.example.moviesapi.mapper;

import com.example.moviesapi.dto.GenreDTO;
import com.example.moviesapi.dto.MovieDTO;
import com.example.moviesapi.entity.Genre;
import com.example.moviesapi.entity.Movie;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GenreMapper {
    private final MovieMapper movieMapper;

    public GenreMapper(@Lazy MovieMapper movieMapper) {
        this.movieMapper = movieMapper;
    }

    public GenreDTO toDTO(Genre genre, boolean includeMovies) {
        if (genre == null) {
            return null;
        }

        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());

        if (includeMovies) {
            dto.setMovies(Optional.ofNullable(genre.getMovies())
                    .map(movies -> movies.stream()
                            .map(movie -> toMovieDTO(movie))
                            .collect(Collectors.toSet()))
                    .orElse(Collections.emptySet()));
        }

        return dto;
    }
    private MovieDTO toMovieDTO(Movie movie) {
        if (movie == null) {
            return null;
        }
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setDuration(movie.getDuration());
        return dto;
    }
}