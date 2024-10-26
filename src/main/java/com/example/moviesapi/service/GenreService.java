package com.example.moviesapi.service;

import com.example.moviesapi.entity.Genre;
import com.example.moviesapi.exception.ResourceAlreadyExistsException;
import com.example.moviesapi.exception.ResourceNotFoundException;
import com.example.moviesapi.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre createGenre(Genre genre) {
        if (genreRepository.existsByNameIgnoreCase(genre.getName())) {
            throw new ResourceAlreadyExistsException(
                    "Genre with name '" + genre.getName() + "' already exists"
            );
        }
        return genreRepository.save(genre);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
    }

    public Genre updateGenre(Long id, Genre genreDetails) {
        Genre genre = getGenreById(id);

        // Check if new name conflicts with existing genre (excluding current genre)
        if (!genre.getName().equalsIgnoreCase(genreDetails.getName()) &&
                genreRepository.existsByNameIgnoreCase(genreDetails.getName())) {
            throw new ResourceAlreadyExistsException(
                    "Genre with name '" + genreDetails.getName() + "' already exists"
            );
        }

        genre.setName(genreDetails.getName());
        return genreRepository.save(genre);
    }

    public void deleteGenre(Long id, boolean force) {
        Genre genre = getGenreById(id);

        if (!force && !genre.getMovies().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete genre '" + genre.getName() +
                            "' because it has " + genre.getMovies().size() + " associated movies"
            );
        }

        genreRepository.deleteById(id);
    }
}