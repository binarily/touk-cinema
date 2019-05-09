package pl.czerniak.cinema.data.controllers;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.czerniak.cinema.data.assemblers.FilmResourceAssembler;
import pl.czerniak.cinema.data.exceptions.NotFoundException;
import pl.czerniak.cinema.data.objects.Film;
import pl.czerniak.cinema.data.repositories.FilmRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public
class FilmController {

    private final FilmRepository repository;
    private final FilmResourceAssembler assembler;

    FilmController(FilmRepository repository, FilmResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    //LIST

    @GetMapping(path="/films", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resources<Resource<Film>> all() {

        List<Resource<Film>> films = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(films,
                linkTo(methodOn(FilmController.class).all()).withSelfRel());
    }

    // Single item

    @GetMapping(path="/films/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resource<Film> one(@PathVariable Long id) {
        return assembler.toResource(
                repository.findById(id)
                        .orElseThrow(() -> new NotFoundException("film", id)));
    }

    //ADD

    @PostMapping(path="/films", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Resource<Film>> newFilm(@RequestBody Film film) {

        Film newFilm = repository.save(film);

        return ResponseEntity
                .created(linkTo(methodOn(FilmController.class).one(newFilm.getId())).toUri())
                .body(assembler.toResource(newFilm));
    }

    //DELETE
    @DeleteMapping(path="/films/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity <?> deleteFilm(@PathVariable Long id) {
        return repository.findById(id).map(p -> {
            repository.deleteById(id);
            //TODO: remove all related screenings
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new NotFoundException("film", id));
    }
}