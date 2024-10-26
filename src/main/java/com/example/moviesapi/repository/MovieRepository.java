package com.example.moviesapi.repository;

import com.example.moviesapi.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findByGenresId(Long genreId, Pageable pageable);
    Page<Movie> findByReleaseYear(Integer year, Pageable pageable);
    Page<Movie> findByActorsId(Long actorId, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Movie> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);
}