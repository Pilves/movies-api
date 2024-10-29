package com.example.moviesapi.service;

import com.example.moviesapi.entity.Genre;
import com.example.moviesapi.exception.ApiException;
import com.example.moviesapi.repository.GenreRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GenreService {
    private static final Logger logger = LoggerFactory.getLogger(GenreService.class);
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional
    public Genre createGenre(Genre genre) {
        validateGenre(genre);

        String normalizedName = normalizeGenreName(genre.getName());
        if (genreRepository.existsByNameIgnoreCase(normalizedName)) {
            logger.warn("Attempted to create duplicate genre: {}", normalizedName);
            throw ApiException.duplicate("Genre already exists with name: " + normalizedName);
        }

        genre.setName(normalizedName);

        try {
            Genre savedGenre = genreRepository.save(genre);
            logger.info("Created new genre: {}", savedGenre.getName());
            return savedGenre;
        } catch (DataIntegrityViolationException e) {
            logger.error("Database constraint violation while creating genre: {}", normalizedName, e);
            throw ApiException.duplicate("Genre already exists with name: " + normalizedName);
        }
    }

    public Page<Genre> getAllGenres(Pageable pageable) {
        try {
            return genreRepository.findAll(pageable);
        } catch (Exception e) {
            logger.error("Failed to fetch genres", e);
            throw ApiException.operationFailed("Failed to fetch genres");
        }
    }

    public Genre getGenreById(Long id) {
        if (id == null) {
            throw ApiException.invalidRequest("Genre ID cannot be null");
        }

        return genreRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Genre not found with id: {}", id);
                    return ApiException.notFound("Genre not found with id: " + id);
                });
    }

    @Transactional
    public Genre updateGenre(Long id, Genre genreDetails) {
        Genre existingGenre = getGenreById(id);

        if (genreDetails.getName() == null || genreDetails.getName().trim().isEmpty()) {
            throw ApiException.invalidRequest("Genre name cannot be empty");
        }

        String normalizedName = normalizeGenreName(genreDetails.getName());

        if (!existingGenre.getName().equalsIgnoreCase(normalizedName) &&
                genreRepository.existsByNameIgnoreCase(normalizedName)) {
            throw ApiException.duplicate("Genre already exists with name: " + normalizedName);
        }

        try {
            existingGenre.setName(normalizedName);
            Genre updatedGenre = genreRepository.save(existingGenre);
            logger.info("Updated genre id: {} to name: {}", id, normalizedName);
            return updatedGenre;
        } catch (DataIntegrityViolationException e) {
            logger.error("Database constraint violation while updating genre: {}", normalizedName, e);
            throw ApiException.duplicate("Genre already exists with name: " + normalizedName);
        }
    }

    @Transactional
    public void deleteGenre(Long id, boolean force) {
        Genre genre = getGenreById(id);

        if (!force && !genre.getMovies().isEmpty()) {
            throw ApiException.invalidRequest(
                    "Cannot delete genre with associated movies. Use force=true to delete anyway."
            );
        }

        try {
            genreRepository.delete(genre);
            logger.info("Deleted genre: {}", genre.getName());
        } catch (Exception e) {
            logger.error("Failed to delete genre with id: {}", id, e);
            throw ApiException.operationFailed("Failed to delete genre");
        }
    }

    private void validateGenre(Genre genre) {
        if (genre == null) {
            throw ApiException.invalidRequest("Genre cannot be null");
        }
        if (genre.getName() == null || genre.getName().trim().isEmpty()) {
            throw ApiException.invalidRequest("Genre name cannot be empty");
        }
    }

    private String normalizeGenreName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw ApiException.invalidRequest("Genre name cannot be empty");
        }

        String normalized = name.trim().replaceAll("\\s+", " ");
        return normalized.substring(0, 1).toUpperCase() +
                normalized.substring(1).toLowerCase();
    }
}