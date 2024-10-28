package com.example.moviesapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter @Setter
@Table(name = "actors", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Actor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Column(unique = true)
    private String name;
    @NotNull @Past
    private LocalDate birthDate;
    @ManyToMany(mappedBy = "actors")
    private Set<Movie> movies;

    @PrePersist @PreUpdate
    private void prepareName() {
        if (name != null) {
            name = name.trim().replaceAll("\\s+", " ");
            String[] words = name.split(" ");
            StringBuilder normalizedName = new StringBuilder();
            for (String word : words) {
                if (normalizedName.length() > 0) normalizedName.append(" ");
                normalizedName.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
            }
            name = normalizedName.toString();
        }
    }
}