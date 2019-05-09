package pl.czerniak.cinema.data.controllers;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.czerniak.cinema.data.assemblers.ScreeningResourceAssembler;
import pl.czerniak.cinema.data.exceptions.NotFoundException;
import pl.czerniak.cinema.data.objects.Film;
import pl.czerniak.cinema.data.objects.Room;
import pl.czerniak.cinema.data.objects.Screening;
import pl.czerniak.cinema.data.repositories.FilmRepository;
import pl.czerniak.cinema.data.repositories.RoomRepository;
import pl.czerniak.cinema.data.repositories.ScreeningRepository;
import pl.czerniak.cinema.data.requests.ScreeningRequest;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public
class ScreeningController {

    private final ScreeningRepository repository;
    private final FilmRepository filmRepository;
    private final RoomRepository roomRepository;
    private final ScreeningResourceAssembler assembler;

    ScreeningController(ScreeningRepository repository, FilmRepository filmRepository,
                          RoomRepository roomRepository, ScreeningResourceAssembler assembler) {
        this.repository = repository;
        this.filmRepository = filmRepository;
        this.roomRepository = roomRepository;
        this.assembler = assembler;
    }

    //LIST

    @GetMapping(path = "/screenings", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resources<Resource<Screening>> all() {

        List<Resource<Screening>> screenings = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(screenings,
                linkTo(methodOn(ScreeningController.class).all()).withSelfRel());
    }

    // Single item

    @GetMapping(path = "/screenings/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resource<Screening> one(@PathVariable Long id) {
        return assembler.toResource(
                repository.findById(id)
                        .orElseThrow(() -> new NotFoundException("screenings", id)));
    }

    //ADD

    @PostMapping(path = "/screenings", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Resource<Screening>> newScreening(@RequestBody ScreeningRequest request) {
        Film film = filmRepository.getOne(request.getFilmId());
        Room room = roomRepository.getOne(request.getRoomId());

        Screening screening = new Screening(film, room, request.getDate());
        Screening newScreening = repository.save(screening);
        return ResponseEntity
                .created(linkTo(methodOn(ScreeningController.class).one(newScreening.getId())).toUri())
                .body(assembler.toResource(newScreening));
    }

    //DELETE

    @DeleteMapping(path = "/screenings/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity <?> deleteScreening(@PathVariable Long id) {
        return repository.findById(id).map(p -> {
            repository.deleteById(id);
            //TODO: remove all related seat reservations, reservations
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new NotFoundException("screening", id));
    }
}