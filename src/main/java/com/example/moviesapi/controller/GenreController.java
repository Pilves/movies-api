package com.example.moviesapi.controller;

import com.example.moviesapi.entity.Genre;
import com.example.moviesapi.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.example.moviesapi.dto.GenreDTO;
import com.example.moviesapi.entity.Genre;
import com.example.moviesapi.service.GenreService;
import com.example.moviesapi.mapper.GenreMapper;


@RestController
@RequestMapping("/api/genres")
public class GenreController {
    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @Autowired
    public GenreController(GenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(@Valid @RequestBody Genre genre) {
        return ResponseEntity.status(201).body(genreService.createGenre(genre));
    }

    @GetMapping
    public ResponseEntity<Page<Genre>> getAllGenres(Pageable pageable) {
        return ResponseEntity.ok(genreService.getAllGenres(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.getGenreById(id));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<GenreDTO> getGenreWithMovies(@PathVariable Long id) {
        Genre genre = genreService.getGenreById(id);
        return ResponseEntity.ok(genreMapper.toDTO(genre));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @RequestBody Genre genreDetails) {
        return ResponseEntity.ok(genreService.updateGenre(id, genreDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean force) {
        genreService.deleteGenre(id, force);
        return ResponseEntity.noContent().build();
    }
}