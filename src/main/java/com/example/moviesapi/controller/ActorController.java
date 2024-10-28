package com.example.moviesapi.controller;

import com.example.moviesapi.dto.ActorDTO;
import com.example.moviesapi.dto.ActorSummaryDTO;
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
        return actorMapper.toDTO(actorService.createActor(actor));
    }

    @GetMapping
    public ResponseEntity<Page<ActorSummaryDTO>> getAllActors(@RequestParam(required = false) String name, Pageable pageable) {
        Page<Actor> actorPage = name != null ? actorService.getActorsByName(name, pageable) : actorService.getAllActors(pageable);
        Page<ActorSummaryDTO> summaryPage = actorPage.map(this::toSummaryDTO);
        return ResponseEntity.ok(summaryPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorSummaryDTO> getActorById(@PathVariable Long id) {
        return ResponseEntity.ok(toSummaryDTO(actorService.getActorById(id)));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ActorDTO> getActorDetails(@PathVariable Long id) {
        return ResponseEntity.ok(actorMapper.toDTO(actorService.getActorById(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ActorDTO> updateActor(@PathVariable Long id, @RequestBody Actor actorDetails) {
        return ResponseEntity.ok(actorMapper.toDTO(actorService.updateActor(id, actorDetails)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force) {
        actorService.deleteActor(id, force);
        return ResponseEntity.noContent().build();
    }

    private ActorSummaryDTO toSummaryDTO(Actor actor) {
        ActorSummaryDTO dto = new ActorSummaryDTO();
        dto.setId(actor.getId());
        dto.setName(actor.getName());
        dto.setBirthDate(String.valueOf(actor.getBirthDate()));
        return dto;
    }
}