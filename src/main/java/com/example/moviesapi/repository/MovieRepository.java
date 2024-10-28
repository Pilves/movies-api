package com.example.moviesapi.repository;

import com.example.moviesapi.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.actors WHERE m.id = :id")
    Optional<Movie> findByIdWithActors(Long id);
    Page<Movie> findByGenresId(Long genreId, Pageable pageable);
    Page<Movie> findByReleaseYear(Integer releaseYear, Pageable pageable);
    Page<Movie> findByActorsId(Long actorId, Pageable pageable);
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    @EntityGraph(attributePaths = "actors")
    Optional<Movie> findById(Long id);
}