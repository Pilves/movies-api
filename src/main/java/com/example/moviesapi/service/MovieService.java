package com.example.moviesapi.service;

import com.example.moviesapi.entity.*;
import com.example.moviesapi.exception.ResourceNotFoundException;
import com.example.moviesapi.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;
    private final EntityManager entityManager;

    public MovieService(MovieRepository movieRepository,
                        ActorRepository actorRepository,
                        GenreRepository genreRepository,
                        EntityManager entityManager) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.genreRepository = genreRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public Movie createMovie(Movie movie) {
        if (movie.getGenres() != null) {
            movie.setGenres(movie.getGenres().stream()
                    .map(genre -> genreRepository.findById(genre.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Genre not found")))
                    .collect(Collectors.toSet()));
        }
        if (movie.getActors() != null) {
            movie.setActors(movie.getActors().stream()
                    .map(actor -> actorRepository.findById(actor.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Actor not found")))
                    .collect(Collectors.toSet()));
        }
        return movieRepository.save(movie);
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Movie getMovieById(Long id) {
        return movieRepository.findByIdWithActors(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
    }

    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = getMovieById(id);
        movie.setTitle(movieDetails.getTitle());
        movie.setReleaseYear(movieDetails.getReleaseYear());
        movie.setDuration(movieDetails.getDuration());
        movie.setGenres(movieDetails.getGenres());
        movie.setActors(movieDetails.getActors());
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public Page<Movie> getMoviesByGenreId(Long genreId, Pageable pageable) {
        return movieRepository.findByGenresId(genreId, pageable);
    }

    public Page<Movie> getMoviesByReleaseYear(Integer releaseYear, Pageable pageable) {
        return movieRepository.findByReleaseYear(releaseYear, pageable);
    }

    public Page<Movie> getMoviesByActorId(Long actorId, Pageable pageable) {
        return movieRepository.findByActorsId(actorId, pageable);
    }

    public Page<Movie> searchMoviesByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
}