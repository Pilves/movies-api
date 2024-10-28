package com.example.moviesapi.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.moviesapi.repository.MovieRepository;
import com.example.moviesapi.repository.ActorRepository;
import com.example.moviesapi.repository.GenreRepository;
import com.example.moviesapi.entity.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class ExportService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ExportService(
            MovieRepository movieRepository,
            ActorRepository actorRepository,
            GenreRepository genreRepository,
            ObjectMapper objectMapper) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.genreRepository = genreRepository;
        this.objectMapper = objectMapper;
    }

    public byte[] exportMoviesToJson() throws IOException {
        List<Movie> movies = movieRepository.findAll();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(movies);
    }

    public byte[] exportMoviesToCsv() throws IOException {
        List<Movie> movies = movieRepository.findAll();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(output);

        // Write CSV header
        writer.println("ID,Title,Release Year,Duration,Genres,Actors");

        // Write movie data
        for (Movie movie : movies) {
            writer.println(String.format("%d,%s,%d,%d,\"%s\",\"%s\"",
                    movie.getId(),
                    escapeCsvField(movie.getTitle()),
                    movie.getReleaseYear(),
                    movie.getDuration(),
                    getGenreNames(movie),
                    getActorNames(movie)
            ));
        }

        writer.flush();
        return output.toByteArray();
    }

    private String escapeCsvField(String field) {
        return field.replace("\"", "\"\"");
    }

    private String getGenreNames(Movie movie) {
        return movie.getGenres().stream()
                .map(genre -> genre.getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }

    private String getActorNames(Movie movie) {
        return movie.getActors().stream()
                .map(actor -> actor.getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
}