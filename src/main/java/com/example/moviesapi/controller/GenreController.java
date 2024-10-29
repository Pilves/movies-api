package com.example.moviesapi.controller;

import com.example.moviesapi.dto.GenreDTO;
import com.example.moviesapi.entity.Genre;
import com.example.moviesapi.service.GenreService;
import com.example.moviesapi.mapper.GenreMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/genres")
public class GenreController {
    private final GenreService genreService;
    private final GenreMapper genreMapper;

    public GenreController(GenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    @PostMapping
    public ResponseEntity<GenreDTO> createGenre(@Valid @RequestBody Genre genre) {
        return ResponseEntity.status(201)
                .body(genreMapper.toDTO(genreService.createGenre(genre), true));
    }

    @GetMapping
    public ResponseEntity<Page<GenreDTO>> getAllGenres(Pageable pageable) {
        return ResponseEntity.ok(genreService.getAllGenres(pageable)
                .map(genre -> genreMapper.toDTO(genre, false)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable Long id) {
        return ResponseEntity.ok(genreMapper.toDTO(genreService.getGenreById(id), false));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<GenreDTO> getGenreWithMovies(@PathVariable Long id) {
        return ResponseEntity.ok(genreMapper.toDTO(genreService.getGenreById(id), true));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GenreDTO> updateGenre(@PathVariable Long id, @RequestBody Genre genreDetails) {
        return ResponseEntity.ok(genreMapper.toDTO(genreService.updateGenre(id, genreDetails), true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean force) {
        genreService.deleteGenre(id, force);
        return ResponseEntity.noContent().build();
    }
}