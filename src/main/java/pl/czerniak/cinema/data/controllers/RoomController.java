package pl.czerniak.cinema.data.controllers;

import org.springframework.web.bind.annotation.*;
import pl.czerniak.cinema.data.exceptions.NotFoundException;
import pl.czerniak.cinema.data.objects.Room;
import pl.czerniak.cinema.data.repositories.RoomRepository;

import java.util.List;

@RestController
class RoomController {

    private final RoomRepository repository;

    RoomController(RoomRepository repository) {
        this.repository = repository;
    }

    //LIST

    @GetMapping("/rooms")
    List<Room> all() {
        return repository.findAll();
    }

    @GetMapping("/rooms/{id}")
    Room one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("room", id));
    }

    //ADD

    @PostMapping("/rooms")
    Room newRoom(@RequestBody Room newRoom) {
        return repository.save(newRoom);
    }

    //REPLACE

    @PutMapping("/rooms/{id}")
    Room replaceFilm(@RequestBody Room newRoom, @PathVariable Long id) {

        return repository.findById(id)
                .map(room -> {
                    room.setName(newRoom.getName());
                    room.setRows(newRoom.getRows());
                    room.setSeatsInARow(newRoom.getSeatsInARow());
                    return repository.save(room);
                })
                .orElseGet(() -> {
                    newRoom.setId(id);
                    return repository.save(newRoom);
                });
    }

    //DELETE

    @DeleteMapping("/rooms/{id}")
    void deleteFilm(@PathVariable Long id) {
        repository.deleteById(id);
        //TODO: remove all related screenings
    }
}