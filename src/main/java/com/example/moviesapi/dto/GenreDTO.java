package com.example.moviesapi.dto;

import lombok.Data;
import java.util.Set;

@Data
public class GenreDTO {
    private Long id;
    private String name;
    private Set<MovieDTO> movies;
}