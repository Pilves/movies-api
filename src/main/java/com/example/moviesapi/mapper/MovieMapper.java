package com.example.moviesapi.mapper;

import com.example.moviesapi.dto.MovieDTO;
import com.example.moviesapi.entity.Movie;
import com.example.moviesapi.entity.Actor;
import com.example.moviesapi.entity.Genre;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Optional;

@Component
public class MovieMapper {

    public MovieDTO toDTO(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setDuration(movie.getDuration());

        dto.setGenres(Optional.ofNullable(movie.getGenres())
                .map(genres -> genres.stream()
                        .map(this::toGenreSummary)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet()));

        dto.setActors(Optional.ofNullable(movie.getActors())
                .map(actors -> actors.stream()
                        .map(this::toActorSummary)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet()));

        return dto;
    }

    private MovieDTO.GenreSummaryDTO toGenreSummary(Genre genre) {
        if (genre == null) return null;

        MovieDTO.GenreSummaryDTO summary = new MovieDTO.GenreSummaryDTO();
        summary.setId(genre.getId());
        summary.setName(genre.getName());
        return summary;
    }

    private MovieDTO.ActorSummaryDTO toActorSummary(Actor actor) {
        if (actor == null) return null;

        MovieDTO.ActorSummaryDTO summary = new MovieDTO.ActorSummaryDTO();
        summary.setId(actor.getId());
        summary.setName(actor.getName());
        summary.setBirthDate(Optional.ofNullable(actor.getBirthDate())
                .map(Object::toString)
                .orElse(null));
        return summary;
    }
}