package com.example.moviesapi.controller;

import com.example.moviesapi.dto.ActorDTO;
import com.example.moviesapi.dto.MovieDTO;
import com.example.moviesapi.entity.Movie;
import com.example.moviesapi.service.MovieService;
import com.example.moviesapi.mapper.MovieMapper;
import com.example.moviesapi.mapper.ActorMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.moviesapi.exception.ApiException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final MovieMapper movieMapper;
    private final ActorMapper actorMapper;

    public MovieController(MovieService movieService, MovieMapper movieMapper,
                           ActorMapper actorMapper) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.actorMapper = actorMapper;
    }

    @GetMapping
    public ResponseEntity<Page<MovieDTO>> getAllMovies(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String actor,
            @RequestParam(defaultValue = "false") boolean includeRelations,
            Pageable pageable) {
        Page<Movie> moviePage;

        try {
            if (year != null) {
                moviePage = movieService.getMoviesByReleaseYear(year, pageable);
            } else if (genre != null) {
                Long genreId = Long.parseLong(genre);
                moviePage = movieService.getMoviesByGenreId(genreId, pageable);
            } else if (actor != null) {
                Long actorId = Long.parseLong(actor);
                moviePage = movieService.getMoviesByActorId(actorId, pageable);
            } else {
                moviePage = movieService.getAllMovies(pageable);
            }

            return ResponseEntity.ok(moviePage.map(movie ->
                    movieMapper.toDTO(movie, includeRelations)));

        } catch (NumberFormatException e) {
            throw ApiException.invalidRequest("Invalid ID format for genre or actor");
        }
    }

    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody Movie movie) {
        Movie createdMovie = movieService.createMovie(movie);
        return ResponseEntity.status(201)
                .body(movieMapper.toDTO(createdMovie, true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean includeRelations) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movieMapper.toDTO(movie, includeRelations));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<MovieDTO> getMovieDetails(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movieMapper.toDTO(movie, true));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(
            @PathVariable Long id,
            @RequestBody MovieDTO movieDetails) {
        Movie updatedMovie = movieService.updateMovie(id, movieDetails);
        return ResponseEntity.ok(movieMapper.toDTO(updatedMovie, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean force) {
        movieService.deleteMovie(id, force);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MovieDTO>> searchMovies(
            @RequestParam String title,
            @RequestParam(defaultValue = "false") boolean includeRelations,
            Pageable pageable) {
        Page<Movie> moviePage = movieService.searchMoviesByTitle(title, pageable);
        return ResponseEntity.ok(moviePage.map(movie ->
                movieMapper.toDTO(movie, includeRelations)));
    }

    @GetMapping("/{id}/actors")
    public ResponseEntity<List<ActorDTO>> getActorsInMovie(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        List<ActorDTO> actors = movie.getActors().stream()
                .map(actor -> actorMapper.toDTO(actor, false))
                .collect(Collectors.toList());
        return ResponseEntity.ok(actors);
    }
}