package com.example.moviesapi.mapper;

import com.example.moviesapi.dto.MovieDTO;
import com.example.moviesapi.dto.ActorDTO;
import com.example.moviesapi.dto.GenreDTO;
import com.example.moviesapi.entity.Movie;
import com.example.moviesapi.entity.Genre;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MovieMapper {
    private final ActorMapper actorMapper;
    private final GenreMapper genreMapper;

    public MovieMapper(ActorMapper actorMapper, @Lazy GenreMapper genreMapper) {
        this.actorMapper = actorMapper;
        this.genreMapper = genreMapper;
    }

    public MovieDTO toDTO(Movie movie, boolean includeRelations) {
        if (movie == null) {
            return null;
        }

        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setDuration(movie.getDuration());

        if (includeRelations) {
            dto.setActors(Optional.ofNullable(movie.getActors())
                    .map(actors -> actors.stream()
                            .map(actor -> actorMapper.toDTO(actor, false))
                            .collect(Collectors.toSet()))
                    .orElse(Collections.emptySet()));

            dto.setGenres(Optional.ofNullable(movie.getGenres())
                    .map(genres -> genres.stream()
                            .map(genre -> toGenreDTO(genre))
                            .collect(Collectors.toSet()))
                    .orElse(Collections.emptySet()));
        }

        return dto;
    }
    private GenreDTO toGenreDTO(Genre genre) {
        if (genre == null) {
            return null;
        }
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }
}