package com.example.moviesapi.repository;

import com.example.moviesapi.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM Genre g WHERE LOWER(g.name) = LOWER(:name)")
    boolean existsByNameIgnoreCase(String name);
}