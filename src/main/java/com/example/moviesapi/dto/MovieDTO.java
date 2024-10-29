package com.example.moviesapi.dto;

import lombok.Data;
import java.util.Set;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private Integer releaseYear;
    private Integer duration;
    private Set<GenreDTO> genres;
    private Set<ActorDTO> actors;
}