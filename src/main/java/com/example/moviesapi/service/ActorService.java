package com.example.moviesapi.service;

import com.example.moviesapi.entity.Actor;
import com.example.moviesapi.repository.ActorRepository;
import com.example.moviesapi.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class ActorService {
    private final ActorRepository actorRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Transactional
    public Actor createActor(Actor actor) {
        validateActor(actor);
        return actorRepository.save(actor);
    }

    public Page<Actor> getAllActors(Pageable pageable) {
        return actorRepository.findAll(pageable);
    }

    public Actor getActorById(Long id) {
        if (id == null) {
            throw ApiException.invalidRequest("Actor ID cannot be null");
        }
        return actorRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Actor not found with id: " + id));
    }

    @Transactional
    public Actor updateActor(Long id, Actor actorDetails) {
        Actor actor = getActorById(id);

        if (actorDetails.getName() != null) {
            actor.setName(actorDetails.getName());
        }

        if (actorDetails.getBirthDate() != null) {
            if (actorDetails.getBirthDate().isAfter(LocalDate.now())) {
                throw ApiException.invalidRequest("Birth date cannot be in the future");
            }
            actor.setBirthDate(actorDetails.getBirthDate());
        }

        validateActor(actor);
        return actorRepository.save(actor);
    }

    @Transactional
    public void deleteActor(Long id, boolean force) {
        Actor actor = getActorById(id);
        if (!force && !actor.getMovies().isEmpty()) {
            throw ApiException.invalidRequest(
                    "Cannot delete actor with associated movies. Use force=true to delete anyway."
            );
        }
        actorRepository.delete(actor);
    }

    public Page<Actor> getActorsByName(String name, Pageable pageable) {
        if (name == null || name.trim().isEmpty()) {
            throw ApiException.invalidRequest("Search name cannot be empty");
        }
        return actorRepository.findByNameContainingIgnoreCase(name.trim(), pageable);
    }

    private void validateActor(Actor actor) {
        if (actor.getName() == null || actor.getName().trim().isEmpty()) {
            throw ApiException.invalidRequest("Actor name cannot be empty");
        }

        if (actor.getBirthDate() == null) {
            throw ApiException.invalidRequest("Birth date cannot be null");
        }

        if (actor.getBirthDate().isAfter(LocalDate.now())) {
            throw ApiException.invalidRequest("Birth date cannot be in the future");
        }
    }
}