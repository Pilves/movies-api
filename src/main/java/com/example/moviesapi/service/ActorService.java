package com.example.moviesapi.service;

import com.example.moviesapi.entity.Actor;
import com.example.moviesapi.exception.ResourceNotFoundException;
import com.example.moviesapi.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActorService {

    private final ActorRepository actorRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Actor createActor(Actor actor) {
        return actorRepository.save(actor);
    }

    public Page<Actor> getAllActors(Pageable pageable) {
        return actorRepository.findAll(pageable);
    }

    public Actor getActorById(Long id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    }

    public Page<Actor> searchActorsByName(String name, Pageable pageable) {
        return actorRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Actor updateActor(Long id, Actor actorDetails) {
        Actor actor = getActorById(id);

        actor.setName(actorDetails.getName());
        actor.setBirthDate(actorDetails.getBirthDate());

        return actorRepository.save(actor);
    }

    public void deleteActor(Long id, boolean force) {
        Actor actor = getActorById(id);

        if (!force && !actor.getMovies().isEmpty()) {
            throw new IllegalStateException(
                    "Unable to delete actor '" + actor.getName() +
                            "' as they are associated with " + actor.getMovies().size() + " movies"
            );
        }

        actorRepository.deleteById(id);
    }
}