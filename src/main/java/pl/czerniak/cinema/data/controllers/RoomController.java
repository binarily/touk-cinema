package pl.czerniak.cinema.data.controllers;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.czerniak.cinema.data.assemblers.RoomResourceAssembler;
import pl.czerniak.cinema.data.exceptions.notfound.RoomNotFoundException;
import pl.czerniak.cinema.data.objects.Room;
import pl.czerniak.cinema.data.objects.Screening;
import pl.czerniak.cinema.data.repositories.RoomRepository;
import pl.czerniak.cinema.data.repositories.ScreeningRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public
class RoomController {

    private final RoomRepository repository;
    private final ScreeningRepository screeningRepository;
    private final ScreeningController screeningController;
    private final RoomResourceAssembler assembler;

    RoomController(RoomRepository repository, ScreeningRepository screeningRepository,
                   ScreeningController screeningController, RoomResourceAssembler assembler) {
        this.repository = repository;
        this.screeningRepository = screeningRepository;
        this.screeningController = screeningController;
        this.assembler = assembler;
    }

    //LIST

    @GetMapping(path = "/rooms", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resources<Resource<Room>> all() {

        List<Resource<Room>> rooms = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(rooms,
                linkTo(methodOn(RoomController.class).all()).withSelfRel());
    }

    @GetMapping(path = "/rooms/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resource<Room> one(@PathVariable Long id) {
        return assembler.toResource(
                repository.findById(id)
                        .orElseThrow(() -> new RoomNotFoundException(id)));
    }

    //ADD

    @PostMapping(path = "/rooms", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Resource<Room>> newRoom(@RequestBody Room room) {

        Room newRoom = repository.save(room);

        return ResponseEntity
                .created(linkTo(methodOn(RoomController.class).one(newRoom.getId())).toUri())
                .body(assembler.toResource(newRoom));
    }

    //DELETE

    @DeleteMapping(path = "/rooms/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity <?> deleteRoom(@PathVariable Long id) {
        return repository.findById(id).map(p -> {
            //remove all related screenings
            List<Screening> screenings = screeningRepository.findAllByRoomEquals(p);
            for(Screening s: screenings){
                screeningController.deleteScreening(s.getId());
            }
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new RoomNotFoundException(id));
    }
}