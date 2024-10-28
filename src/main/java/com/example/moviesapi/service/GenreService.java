package com.example.moviesapi.service;

import com.example.moviesapi.entity.Genre;
import com.example.moviesapi.exception.*;
import com.example.moviesapi.repository.GenreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional
    public Genre createGenre(Genre genre) {
        if (genre.getName() != null) {
            String normalizedName = genre.getName().trim().toLowerCase();
            if (genreRepository.existsByNameIgnoreCase(normalizedName)) {
                throw new DuplicateResourceException("Genre already exists");
            }
        }
        try {
            return genreRepository.save(genre);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Genre already exists");
        }
    }

    public Page<Genre> getAllGenres(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
    }

    public Genre updateGenre(Long id, Genre genreDetails) {
        Genre genre = getGenreById(id);
        genre.setName(genreDetails.getName());
        return genreRepository.save(genre);
    }

    public void deleteGenre(Long id, boolean force) {
        Genre genre = getGenreById(id);
        if (!force && !genre.getMovies().isEmpty()) {
            throw new IllegalStateException("Cannot delete genre with associated movies");
        }
        genreRepository.delete(genre);
    }
}