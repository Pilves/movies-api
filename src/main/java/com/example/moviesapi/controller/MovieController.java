package com.example.moviesapi.controller;

import com.example.moviesapi.config.PaginationValidator;
import com.example.moviesapi.dto.ActorSummaryDTO;
import com.example.moviesapi.dto.MovieDTO;
import com.example.moviesapi.dto.MovieSummaryDTO;
import com.example.moviesapi.entity.Movie;
import com.example.moviesapi.service.MovieService;
import com.example.moviesapi.mapper.MovieMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final MovieMapper movieMapper;
    private final PaginationValidator paginationValidator;

    public MovieController(MovieService movieService, MovieMapper movieMapper, PaginationValidator paginationValidator) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.paginationValidator = paginationValidator;
    }

    @GetMapping
    public ResponseEntity<Page<MovieSummaryDTO>> getAllMovies(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long genre,
            @RequestParam(required = false) Long actor,
            Pageable pageable) {
        paginationValidator.validatePageable(pageable);
        Page<Movie> moviePage;

        if (year != null) moviePage = movieService.getMoviesByReleaseYear(year, pageable);
        else if (genre != null) moviePage = movieService.getMoviesByGenreId(genre, pageable);
        else if (actor != null) moviePage = movieService.getMoviesByActorId(actor, pageable);
        else moviePage = movieService.getAllMovies(pageable);

        return ResponseEntity.ok(moviePage.map(this::toSummaryDTO));
    }

    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody Movie movie) {
        return ResponseEntity.status(201).body(movieMapper.toDTO(movieService.createMovie(movie)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieSummaryDTO> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(toSummaryDTO(movieService.getMovieById(id)));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<MovieDTO> getMovieDetails(@PathVariable Long id) {
        return ResponseEntity.ok(movieMapper.toDTO(movieService.getMovieById(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long id, @RequestBody Movie movieDetails) {
        return ResponseEntity.ok(movieMapper.toDTO(movieService.updateMovie(id, movieDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MovieSummaryDTO>> searchMovies(
            @RequestParam String title,
            Pageable pageable) {
        paginationValidator.validatePageable(pageable);
        return ResponseEntity.ok(movieService.searchMoviesByTitle(title, pageable).map(this::toSummaryDTO));
    }

    @GetMapping("/{id}/actors")
    public ResponseEntity<List<ActorSummaryDTO>> getActorsInMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id).getActors().stream()
                .map(actor -> {
                    ActorSummaryDTO dto = new ActorSummaryDTO();
                    dto.setId(actor.getId());
                    dto.setName(actor.getName());
                    dto.setBirthDate(actor.getBirthDate() != null ? actor.getBirthDate().toString() : null);
                    return dto;
                })
                .collect(Collectors.toList()));
    }

    private MovieSummaryDTO toSummaryDTO(Movie movie) {
        MovieSummaryDTO summaryDTO = new MovieSummaryDTO();
        summaryDTO.setId(movie.getId());
        summaryDTO.setTitle(movie.getTitle());
        summaryDTO.setReleaseYear(movie.getReleaseYear());
        summaryDTO.setDuration(movie.getDuration());
        return summaryDTO;
    }
}