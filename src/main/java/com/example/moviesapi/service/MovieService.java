package com.example.moviesapi.service;

import com.example.moviesapi.dto.MovieDTO;
import com.example.moviesapi.entity.*;
import com.example.moviesapi.exception.ApiException;
import com.example.moviesapi.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

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
        try {
            validateMovie(movie);

            // Process genres
            if (movie.getGenres() != null) {
                Set<Genre> validGenres = validateAndGetGenres(movie.getGenres());
                movie.setGenres(validGenres);
            }

            // Process actors
            if (movie.getActors() != null) {
                Set<Actor> validActors = validateAndGetActors(movie.getActors());
                movie.setActors(validActors);
            }

            Movie savedMovie = movieRepository.save(movie);
            logger.info("Created new movie: {} ({})", savedMovie.getTitle(), savedMovie.getId());
            return savedMovie;

        } catch (Exception e) {
            logger.error("Failed to create movie: {}", movie.getTitle(), e);
            throw ApiException.operationFailed("Failed to create movie: " + e.getMessage());
        }
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        try {
            return movieRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("Failed to fetch movies", e);
            throw ApiException.operationFailed("Failed to fetch movies");
        }
    }

    @Transactional(readOnly = true)
    public Movie getMovieById(Long id) {
        if (id == null) {
            throw ApiException.invalidRequest("Movie ID cannot be null");
        }

        return movieRepository.findByIdWithActors(id)
                .orElseThrow(() -> {
                    logger.warn("Movie not found with id: {}", id);
                    return ApiException.notFound("Movie not found with id: " + id);
                });
    }

    @Transactional
    public Movie updateMovie(Long id, MovieDTO movieDetails) {
        Movie existingMovie = getMovieById(id);

        try {
            // Update basic fields if provided
            if (movieDetails.getTitle() != null) {
                existingMovie.setTitle(movieDetails.getTitle().trim());
            }

            if (movieDetails.getReleaseYear() != null) {
                validateReleaseYear(movieDetails.getReleaseYear());
                existingMovie.setReleaseYear(movieDetails.getReleaseYear());
            }

            if (movieDetails.getDuration() != null) {
                validateDuration(movieDetails.getDuration());
                existingMovie.setDuration(movieDetails.getDuration());
            }

            // Update actors if provided
            if (movieDetails.getActors() != null) {
                Set<Actor> updatedActors = movieDetails.getActors().stream()
                        .map(actorDTO -> actorRepository.findById(actorDTO.getId())
                                .orElseThrow(() -> ApiException.notFound("Actor not found with id: " + actorDTO.getId())))
                        .collect(Collectors.toSet());

                existingMovie.getActors().clear();
                existingMovie.getActors().addAll(updatedActors);
            }

            // Update genres if provided
            if (movieDetails.getGenres() != null) {
                Set<Genre> updatedGenres = movieDetails.getGenres().stream()
                        .map(genreDTO -> genreRepository.findById(genreDTO.getId())
                                .orElseThrow(() -> ApiException.notFound("Genre not found with id: " + genreDTO.getId())))
                        .collect(Collectors.toSet());

                existingMovie.getGenres().clear();
                existingMovie.getGenres().addAll(updatedGenres);
            }

            Movie updatedMovie = movieRepository.save(existingMovie);
            logger.info("Updated movie: {} ({})", updatedMovie.getTitle(), updatedMovie.getId());
            return updatedMovie;

        } catch (Exception e) {
            logger.error("Failed to update movie with id: {}", id, e);
            throw ApiException.operationFailed("Failed to update movie: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteMovie(Long id, boolean force) {
        Movie movie = getMovieById(id);
        if(!force && !movie.getActors().isEmpty()){
            throw ApiException.invalidRequest("Cannot delete movie with actors. Use force=true to delete");
        }

        try {
            movieRepository.delete(movie);
            logger.info("Deleted movie: {} ({})", movie.getTitle(), movie.getId());
        } catch (Exception e) {
            logger.error("Failed to delete movie with id: {}", id, e);
            throw ApiException.operationFailed("Failed to delete movie");
        }
    }

    public Page<Movie> getMoviesByGenreId(Long genreId, Pageable pageable) {
        try {
            if (!genreRepository.existsById(genreId)) {
                throw ApiException.notFound("Genre not found with id: " + genreId);
            }
            return movieRepository.findByGenresId(genreId, pageable);
        } catch (Exception e) {
            logger.error("Failed to fetch movies by genre id: {}", genreId, e);
            throw ApiException.operationFailed("Failed to fetch movies by genre");
        }
    }

    public Page<Movie> getMoviesByReleaseYear(Integer year, Pageable pageable) {
        try {
            validateReleaseYear(year);
            return movieRepository.findByReleaseYear(year, pageable);
        } catch (Exception e) {
            logger.error("Failed to fetch movies by release year: {}", year, e);
            throw ApiException.operationFailed("Failed to fetch movies by year");
        }
    }

    public Page<Movie> getMoviesByActorId(Long actorId, Pageable pageable) {
        try {
            if (!actorRepository.existsById(actorId)) {
                throw ApiException.notFound("Actor not found with id: " + actorId);
            }
            return movieRepository.findByActorsId(actorId, pageable);
        } catch (Exception e) {
            logger.error("Failed to fetch movies by actor id: {}", actorId, e);
            throw ApiException.operationFailed("Failed to fetch movies by actor");
        }
    }

    public Page<Movie> searchMoviesByTitle(String title, Pageable pageable) {
        try {
            if (title == null || title.trim().isEmpty()) {
                throw ApiException.invalidRequest("Search title cannot be empty");
            }
            return movieRepository.findByTitleContainingIgnoreCase(title.trim(), pageable);
        } catch (Exception e) {
            logger.error("Failed to search movies by title: {}", title, e);
            throw ApiException.operationFailed("Failed to search movies");
        }
    }

    private void validateMovie(Movie movie) {
        if (movie == null) {
            throw ApiException.invalidRequest("Movie cannot be null");
        }
        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
            throw ApiException.invalidRequest("Movie title cannot be empty");
        }
        validateReleaseYear(movie.getReleaseYear());
        validateDuration(movie.getDuration());
    }

    private void validateReleaseYear(Integer year) {
        if (year == null) {
            throw ApiException.invalidRequest("Release year cannot be null");
        }
        int currentYear = LocalDate.now().getYear();
        if (year < 1888 || year > currentYear + 5) {
            throw ApiException.invalidRequest(
                    String.format("Release year must be between 1888 and %d", currentYear + 5)
            );
        }
    }

    private void validateDuration(Integer duration) {
        if (duration == null) {
            throw ApiException.invalidRequest("Duration cannot be null");
        }
        if (duration <= 0 || duration > 1000) {
            throw ApiException.invalidRequest("Duration must be between 1 and 1000 minutes");
        }
    }

    private Set<Genre> validateAndGetGenres(Set<Genre> genres) {
        if (genres.isEmpty()) {
            throw ApiException.invalidRequest("Movie must have at least one genre");
        }

        return genres.stream()
                .map(genre -> genreRepository.findById(genre.getId())
                        .orElseThrow(() -> ApiException.notFound("Genre not found with id: " + genre.getId())))
                .collect(Collectors.toSet());
    }

    private Set<Actor> validateAndGetActors(Set<Actor> actors) {
        if (actors.isEmpty()) {
            throw ApiException.invalidRequest("Movie must have at least one actor");
        }

        return actors.stream()
                .map(actor -> actorRepository.findById(actor.getId())
                        .orElseThrow(() -> ApiException.notFound("Actor not found with id: " + actor.getId())))
                .collect(Collectors.toSet());
    }
}