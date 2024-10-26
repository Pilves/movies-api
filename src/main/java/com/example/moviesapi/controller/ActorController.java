package com.example.moviesapi.controller;

import com.example.moviesapi.dto.PaginationResponse;
import com.example.moviesapi.entity.Actor;
import com.example.moviesapi.service.ActorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @PostMapping
    public ResponseEntity<Actor> createActor(@Valid @RequestBody Actor actor) {
        Actor createdActor = actorService.createActor(actor);
        return new ResponseEntity<>(createdActor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<Actor>> getAllActors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Actor> actorPage = name != null ?
                actorService.searchActorsByName(name, pageRequest) :
                actorService.getAllActors(pageRequest);

        return ResponseEntity.ok(new PaginationResponse<>(actorPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable Long id) {
        Actor actor = actorService.getActorById(id);
        return ResponseEntity.ok(actor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Actor> updateActor(
            @PathVariable Long id,
            @Valid @RequestBody Actor actorDetails) {
        Actor updatedActor = actorService.updateActor(id, actorDetails);
        return ResponseEntity.ok(updatedActor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean force) {
        actorService.deleteActor(id, force);
        return ResponseEntity.noContent().build();
    }
}