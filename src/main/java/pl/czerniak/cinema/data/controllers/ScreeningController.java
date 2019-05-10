package pl.czerniak.cinema.data.controllers;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.czerniak.cinema.data.assemblers.ScreeningResourceAssembler;
import pl.czerniak.cinema.data.exceptions.FilmNotFoundException;
import pl.czerniak.cinema.data.exceptions.RoomNotFoundException;
import pl.czerniak.cinema.data.exceptions.ScreeningNotFoundException;
import pl.czerniak.cinema.data.objects.*;
import pl.czerniak.cinema.data.repositories.*;
import pl.czerniak.cinema.data.requests.ScreeningRequest;

import java.time.LocalDateTime;
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
    private final ReservationRepository reservationRepository;
    private final SeatReservationRepository seatReservationRepository;
    private final ReservationController reservationController;
    private final SeatReservationController seatReservationController;
    private final ScreeningResourceAssembler assembler;

    ScreeningController(ScreeningRepository repository, FilmRepository filmRepository,
                        RoomRepository roomRepository,  ReservationRepository reservationRepository,
                        SeatReservationRepository seatReservationRepository, ReservationController reservationController,
                        SeatReservationController seatReservationController, ScreeningResourceAssembler assembler) {
        this.repository = repository;
        this.filmRepository = filmRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.seatReservationRepository = seatReservationRepository;
        this.reservationController = reservationController;
        this.seatReservationController = seatReservationController;
        this.assembler = assembler;
    }

    //LIST

    // All
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
                        .orElseThrow(() -> new ScreeningNotFoundException(id)));
    }

    // All screenings possible to book after a given hour
    @GetMapping(path="/screenings/after/{dateTimeString}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resources<Resource<Screening>> allStartingFrom(@PathVariable String dateTimeString){
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        if(dateTime.isBefore(LocalDateTime.now().plusMinutes(15))){
            dateTime = LocalDateTime.now().plusMinutes(15);
        }

        List<Resource<Screening>> seatReservations = repository.findAllByStartDateAfter(dateTime).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(seatReservations,
                linkTo(methodOn(ScreeningController.class).allStartingFrom(dateTimeString)).withSelfRel());
    }

    //ADD

    @PostMapping(path = "/screenings", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Resource<Screening>> newScreening(@RequestBody ScreeningRequest request) {
        Film film = filmRepository.findById(request.getFilmId())
                .orElseThrow(() -> new FilmNotFoundException(request.getFilmId()));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException(request.getRoomId()));

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
            //remove all related seat reservations, reservations
            List<SeatReservation> seatReservations = seatReservationRepository.findAllByScreeningEquals(p);
            for(SeatReservation sr : seatReservations){
                //Remove the seat
                seatReservationController.deleteSeatReservation(sr.getId());
                //Remove any empty reservations
                Reservation reservation = reservationRepository.getOne(sr.getReservation().getId());
                if(reservation != null && seatReservationRepository.findAllByReservationEquals(reservation).size() == 0){
                    reservationController.deleteReservation(reservation.getId());
                }
            }
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new ScreeningNotFoundException(id));
    }
}