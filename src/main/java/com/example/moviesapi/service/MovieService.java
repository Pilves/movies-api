package com.example.moviesapi.service;

import com.example.moviesapi.entity.Movie;
import com.example.moviesapi.entity.Genre;
import com.example.moviesapi.entity.Actor;
import com.example.moviesapi.exception.ResourceNotFoundException;
import com.example.moviesapi.repository.MovieRepository;
import com.example.moviesapi.repository.GenreRepository;
import com.example.moviesapi.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository,
                        GenreRepository genreRepository,
                        ActorRepository actorRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
    }

    public Movie createMovie(Movie movie) {
        validateMovieRelations(movie);
        return movieRepository.save(movie);
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }

    public Page<Movie> getMoviesByGenre(Long genreId, Pageable pageable) {
        if (!genreRepository.existsById(genreId)) {
            throw new ResourceNotFoundException("Genre not found with id: " + genreId);
        }
        return movieRepository.findByGenresId(genreId, pageable);
    }

    public Page<Movie> getMoviesByYear(Integer year, Pageable pageable) {
        return movieRepository.findByReleaseYear(year, pageable);
    }

    public Page<Movie> getMoviesByActor(Long actorId, Pageable pageable) {
        if (!actorRepository.existsById(actorId)) {
            throw new ResourceNotFoundException("Actor not found with id: " + actorId);
        }
        return movieRepository.findByActorsId(actorId, pageable);
    }

    public Page<Movie> searchMoviesByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = getMovieById(id);
        validateMovieRelations(movieDetails);

        movie.setTitle(movieDetails.getTitle());
        movie.setReleaseYear(movieDetails.getReleaseYear());
        movie.setDuration(movieDetails.getDuration());
        movie.setGenres(movieDetails.getGenres());
        movie.setActors(movieDetails.getActors());

        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    private void validateMovieRelations(Movie movie) {
        // Validate genres
        Set<Genre> validatedGenres = movie.getGenres().stream()
                .map(genre -> genreRepository.findById(genre.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + genre.getId())))
                .collect(Collectors.toSet());
        movie.setGenres(validatedGenres);

        // Validate actors
        Set<Actor> validatedActors = movie.getActors().stream()
                .map(actor -> actorRepository.findById(actor.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actor.getId())))
                .collect(Collectors.toSet());
        movie.setActors(validatedActors);
    }
}