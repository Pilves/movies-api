package com.example.moviesapi.controller;

import com.example.moviesapi.dto.ActorDTO;
import com.example.moviesapi.entity.Actor;
import com.example.moviesapi.service.ActorService;
import com.example.moviesapi.mapper.ActorMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actors")
public class ActorController {
    private final ActorService actorService;
    private final ActorMapper actorMapper;

    public ActorController(ActorService actorService, ActorMapper actorMapper) {
        this.actorService = actorService;
        this.actorMapper = actorMapper;
    }

    @PostMapping
    public ActorDTO createActor(@RequestBody Actor actor) {
        return actorMapper.toDTO(actorService.createActor(actor), false);
    }

    @GetMapping
    public ResponseEntity<Page<ActorDTO>> getAllActors(
            @RequestParam(required = false) String name,
            Pageable pageable) {
        Page<Actor> actorPage = name != null ?
                actorService.getActorsByName(name, pageable) :
                actorService.getAllActors(pageable);
        return ResponseEntity.ok(actorPage.map(actor -> actorMapper.toDTO(actor, false)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDTO> getActorById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean includeMovies) {
        return ResponseEntity.ok(actorMapper.toDTO(actorService.getActorById(id), includeMovies));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ActorDTO> getActorDetails(@PathVariable Long id) {
        return ResponseEntity.ok(actorMapper.toDTO(actorService.getActorById(id), true));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ActorDTO> updateActor(
            @PathVariable Long id,
            @RequestBody Actor actorDetails) {
        return ResponseEntity.ok(
                actorMapper.toDTO(actorService.updateActor(id, actorDetails), false)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean force) {
        actorService.deleteActor(id, force);
        return ResponseEntity.noContent().build();
    }
}