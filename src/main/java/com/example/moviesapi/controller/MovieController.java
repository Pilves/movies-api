package com.example.moviesapi.controller;

import com.example.moviesapi.dto.PaginationResponse;
import com.example.moviesapi.entity.Movie;
import com.example.moviesapi.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        Movie createdMovie = movieService.createMovie(movie);
        return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<Movie>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long actorId,
            @RequestParam(required = false) String title) {

        Page<Movie> moviePage;
        PageRequest pageRequest = PageRequest.of(page, size);

        if (genreId != null) {
            moviePage = movieService.getMoviesByGenre(genreId, pageRequest);
        } else if (year != null) {
            moviePage = movieService.getMoviesByYear(year, pageRequest);
        } else if (actorId != null) {
            moviePage = movieService.getMoviesByActor(actorId, pageRequest);
        } else if (title != null) {
            moviePage = movieService.searchMoviesByTitle(title, pageRequest);
        } else {
            moviePage = movieService.getAllMovies(pageRequest);
        }

        return ResponseEntity.ok(new PaginationResponse<>(moviePage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(
            @PathVariable Long id,
            @Valid @RequestBody Movie movieDetails) {
        Movie updatedMovie = movieService.updateMovie(id, movieDetails);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}