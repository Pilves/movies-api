package com.example.moviesapi.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;

@Entity
@Getter @Setter
@Table(name = "genres", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Genre {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Pattern(regexp = "^[A-Z][a-zA-Z /-]*$") @Column(unique = true)
    private String name;
    @ManyToMany(mappedBy = "genres")
    @JsonIgnoreProperties("genres")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<Movie> movies;

    @PrePersist @PreUpdate
    private void prepareName() {
        if (name != null) {
            name = name.trim().replaceAll("\\s+", " ");
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
    }
}