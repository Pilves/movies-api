package com.example.moviesapi.service;

import com.example.moviesapi.dto.MovieStatistics;
import com.example.moviesapi.repository.MovieRepository;
import com.example.moviesapi.repository.ActorRepository;
import com.example.moviesapi.repository.GenreRepository;
import com.example.moviesapi.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StatisticsService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public StatisticsService(MovieRepository movieRepository,
                             ActorRepository actorRepository,
                             GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.genreRepository = genreRepository;
    }

    public MovieStatistics generateStatistics() {
        List<Movie> allMovies = movieRepository.findAll();
        MovieStatistics stats = new MovieStatistics();

        // Calculate basic counts
        stats.setTotalMovies(allMovies.size());
        stats.setTotalActors((int) actorRepository.count());
        stats.setTotalGenres((int) genreRepository.count());

        // Calculate movies by year
        Map<Integer, Long> moviesByYear = allMovies.stream()
                .collect(Collectors.groupingBy(
                        Movie::getReleaseYear,
                        Collectors.counting()
                ));
        stats.setMoviesByYear(moviesByYear);

        // Calculate average duration
        double avgDuration = allMovies.stream()
                .mapToInt(Movie::getDuration)
                .average()
                .orElse(0.0);
        stats.setAverageDuration(avgDuration);

        // Calculate average duration by year
        Map<Integer, Double> avgDurationByYear = allMovies.stream()
                .collect(Collectors.groupingBy(
                        Movie::getReleaseYear,
                        Collectors.averagingInt(Movie::getDuration)
                ));
        stats.setAverageDurationByYear(avgDurationByYear);

        // Calculate movies by genre
        Map<String, Long> moviesByGenre = allMovies.stream()
                .flatMap(movie -> movie.getGenres().stream())
                .collect(Collectors.groupingBy(
                        genre -> genre.getName(),
                        Collectors.counting()
                ));
        stats.setMoviesByGenre(moviesByGenre);

        return stats;
    }
}