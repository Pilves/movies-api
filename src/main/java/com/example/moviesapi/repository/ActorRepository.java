package com.example.moviesapi.repository;

import com.example.moviesapi.entity.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Page<Actor> findByNameContainingIgnoreCase(String name, Pageable pageable);
}