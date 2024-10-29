package com.example.moviesapi.mapper;

import com.example.moviesapi.dto.ActorDTO;
import com.example.moviesapi.entity.Actor;
import com.example.moviesapi.entity.Movie;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ActorMapper {

    public ActorDTO toDTO(Actor actor, boolean includeMovies) {
        ActorDTO dto = new ActorDTO();
        dto.setId(actor.getId());
        dto.setName(actor.getName());
        dto.setBirthDate(actor.getBirthDate());

        if (includeMovies) {
            dto.setMovies(Optional.ofNullable(actor.getMovies())
                    .map(movies -> movies.stream()
                            .map(this::toMovieSummary)
                            .collect(Collectors.toSet()))
                    .orElse(Collections.emptySet()));
        }

        return dto;
    }

    private ActorDTO.MovieSummaryDTO toMovieSummary(Movie movie) {
        ActorDTO.MovieSummaryDTO summary = new ActorDTO.MovieSummaryDTO();
        summary.setId(movie.getId());
        summary.setTitle(movie.getTitle());
        summary.setReleaseYear(movie.getReleaseYear());
        summary.setDuration(movie.getDuration());

        if (movie.getGenres() != null) {
            summary.setGenreNames(movie.getGenres().stream()
                    .map(genre -> genre.getName())
                    .collect(Collectors.toSet()));
        }

        return summary;
    }
}