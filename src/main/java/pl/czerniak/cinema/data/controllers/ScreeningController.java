package pl.czerniak.cinema.data.controllers;

import org.springframework.web.bind.annotation.*;
import pl.czerniak.cinema.data.exceptions.NotFoundException;
import pl.czerniak.cinema.data.objects.Film;
import pl.czerniak.cinema.data.objects.Room;
import pl.czerniak.cinema.data.objects.Screening;
import pl.czerniak.cinema.data.repositories.FilmRepository;
import pl.czerniak.cinema.data.repositories.RoomRepository;
import pl.czerniak.cinema.data.repositories.ScreeningRepository;
import pl.czerniak.cinema.data.requests.ScreeningRequest;

import java.util.List;

@RestController
class ScreeningController {

    private final ScreeningRepository repository;
    private final FilmRepository filmRepository;
    private final RoomRepository roomRepository;

    ScreeningController(ScreeningRepository repository, FilmRepository filmRepository,
                          RoomRepository roomRepository) {
        this.repository = repository;
        this.filmRepository = filmRepository;
        this.roomRepository = roomRepository;
    }

    //LIST

    @GetMapping("/screenings")
    List<Screening> all() {
        return repository.findAll();
    }

    // Single item

    @GetMapping("/screenings/{id}")
    Screening one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("screening", id));
    }

    //ADD

    @PostMapping("/screenings")
    Screening newScreening(@RequestBody ScreeningRequest request) {
        Film film = filmRepository.getOne(request.getFilmId());
        Room room = roomRepository.getOne(request.getRoomId());

        Screening newScreening = new Screening(film, room, request.getDate());
        return repository.save(newScreening);
    }

    //REPLACE

    @PutMapping("/screenings/{id}")
    Screening replaceReservation(@RequestBody ScreeningRequest newScreening, @PathVariable Long id) {
        Film film = filmRepository.getOne(newScreening.getFilmId());
        Room room = roomRepository.getOne(newScreening.getRoomId());

        return repository.findById(id)
                .map(screening -> {
                    screening.setDate(newScreening.getDate());
                    screening.setFilm(film);
                    screening.setRoom(room);
                    return repository.save(screening);
                })
                .orElseGet(() -> newScreening(newScreening));
    }

    //DELETE

    @DeleteMapping("/screenings/{id}")
    void deleteReservation(@PathVariable Long id) {
        repository.deleteById(id);
        //TODO: remove all related seat reservations, reservations
    }
}