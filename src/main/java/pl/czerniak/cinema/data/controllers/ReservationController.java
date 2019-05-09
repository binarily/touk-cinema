package pl.czerniak.cinema.data.controllers;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.czerniak.cinema.data.assemblers.ReservationResourceAssembler;
import pl.czerniak.cinema.data.exceptions.NotFoundException;
import pl.czerniak.cinema.data.objects.Reservation;
import pl.czerniak.cinema.data.objects.Screening;
import pl.czerniak.cinema.data.objects.SeatReservation;
import pl.czerniak.cinema.data.repositories.ReservationRepository;
import pl.czerniak.cinema.data.repositories.ScreeningRepository;
import pl.czerniak.cinema.data.repositories.SeatReservationRepository;
import pl.czerniak.cinema.data.requests.ReservationRequest;
import pl.czerniak.cinema.data.requests.Seat;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class ReservationController {

    private final ReservationRepository repository;
    private final ScreeningRepository screeningRepository;
    private final SeatReservationRepository seatReservationRepository;
    private final ReservationResourceAssembler assembler;

    ReservationController(ReservationRepository repository, ScreeningRepository screeningRepository,
                          SeatReservationRepository seatReservationRepository, ReservationResourceAssembler assembler) {
        this.repository = repository;
        this.screeningRepository = screeningRepository;
        this.seatReservationRepository = seatReservationRepository;
        this.assembler = assembler;
    }

    //LIST

    @GetMapping(path = "/reservations", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resources<Resource<Reservation>> all() {

        List<Resource<Reservation>> reservations = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(reservations,
                linkTo(methodOn(ReservationController.class).all()).withSelfRel());
    }

    // Single item

    @GetMapping(path = "/reservations/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resource<Reservation> one(@PathVariable Long id) {
        return assembler.toResource(
                repository.findById(id)
                        .orElseThrow(() -> new NotFoundException("reservation", id)));
    }

    //ADD

    @PostMapping(path = "/reservations", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Resource<Reservation>> newReservation(@RequestBody ReservationRequest request) {
        Screening screening = screeningRepository.getOne(request.getScreeningId());
        //TODO: check if more than 15 minutes to screening start

        Reservation reservation = new Reservation(request.getName(), request.getSurname());
        for(Seat seat: request.getSeats()){
            SeatReservation seatReservation = new SeatReservation(screening, reservation,
                    seat.getTicketType(),seat.getRow(), seat.getColumn());
            //TODO: check if seats are empty
            seatReservationRepository.save(seatReservation);
        }
        Reservation newReservation = repository.save(reservation);

        return ResponseEntity
                .created(linkTo(methodOn(ReservationController.class).one(newReservation.getId())).toUri())
                .body(assembler.toResource(newReservation));
    }

    //DELETE
    @DeleteMapping(path = "/reservations/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity <?> deleteReservation(@PathVariable Long id) {
        return repository.findById(id).map(p -> {
            repository.deleteById(id);
            //TODO: remove all related seat reservations
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new NotFoundException("reservation", id));
    }
}