package pl.czerniak.cinema.data.controllers;

import org.springframework.web.bind.annotation.*;
import pl.czerniak.cinema.data.exceptions.NotFoundException;
import pl.czerniak.cinema.data.objects.Film;
import pl.czerniak.cinema.data.repositories.FilmRepository;

import java.util.List;

@RestController
class FilmController {

    private final FilmRepository repository;

    FilmController(FilmRepository repository) {
        this.repository = repository;
    }

    //LIST

    @GetMapping("/films")
    List<Film> all() {
        return repository.findAll();
    }

    // Single item

    @GetMapping("/films/{id}")
    Film one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("film", id));
    }

    //ADD

    @PostMapping("/films")
    Film newFilm(@RequestBody Film newFilm) {
        return repository.save(newFilm);
    }

    //REPLACE

    @PutMapping("/films/{id}")
    Film replaceFilm(@RequestBody Film newFilm, @PathVariable Long id) {

        return repository.findById(id)
                .map(film -> {
                    film.setTitle(newFilm.getTitle());
                    return repository.save(film);
                })
                .orElseGet(() -> {
                    newFilm.setId(id);
                    return repository.save(newFilm);
                });
    }

    //DELETE

    @DeleteMapping("/films/{id}")
    void deleteFilm(@PathVariable Long id) {
        repository.deleteById(id);
        //TODO: remove all related screenings
    }
}