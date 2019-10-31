package pl.czerniak.cinema.data.controllers;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.czerniak.cinema.data.assemblers.SeatReservationResourceAssembler;
import pl.czerniak.cinema.data.exceptions.notfound.ReservationNotFoundException;
import pl.czerniak.cinema.data.exceptions.notfound.ScreeningNotFoundException;
import pl.czerniak.cinema.data.exceptions.notfound.SeatReservationNotFoundException;
import pl.czerniak.cinema.data.objects.Reservation;
import pl.czerniak.cinema.data.objects.Screening;
import pl.czerniak.cinema.data.objects.SeatReservation;
import pl.czerniak.cinema.data.repositories.ReservationRepository;
import pl.czerniak.cinema.data.repositories.ScreeningRepository;
import pl.czerniak.cinema.data.repositories.SeatReservationRepository;
import pl.czerniak.cinema.data.requests.SeatReservationRequest;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public
class SeatReservationController {

    private final SeatReservationRepository repository;
    private final ScreeningRepository screeningRepository;
    private final ReservationRepository reservationRepository;
    private final SeatReservationResourceAssembler assembler;

    SeatReservationController(SeatReservationRepository repository,
                              ScreeningRepository screeningRepository,
                              ReservationRepository reservationRepository,
                              SeatReservationResourceAssembler assembler) {
        this.repository = repository;
        this.screeningRepository = screeningRepository;
        this.reservationRepository = reservationRepository;
        this.assembler = assembler;
    }

    //LIST

    // All
    @GetMapping(path="/seat_reservations", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resources<Resource<SeatReservation>> all() {
        List<Resource<SeatReservation>> seatReservations = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(seatReservations,
                linkTo(methodOn(SeatReservationController.class).all()).withSelfRel());
    }

    // Single item
    @GetMapping(path="/seat_reservations/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resource<SeatReservation> one(@PathVariable Long id) {
        return assembler.toResource(
                repository.findById(id)
                        .orElseThrow(() -> new SeatReservationNotFoundException(id)));
    }

    // All from a single reservation
    @GetMapping(path="/reservations/{reservationId}/seats", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resources<Resource<SeatReservation>> allFromReservation(@PathVariable Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
        List<Resource<SeatReservation>> seatReservations = repository.findAllByReservationEquals(reservation).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(seatReservations,
                linkTo(methodOn(SeatReservationController.class).allFromReservation(reservationId)).withSelfRel());
    }

    // All from a single screening
    @GetMapping(path="/screenings/{screeningId}/seats", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Resources<Resource<SeatReservation>> allFromScreening(@PathVariable Long screeningId){
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new ScreeningNotFoundException(screeningId));
        List<Resource<SeatReservation>> seatReservations = repository.findAllByScreeningEquals(screening).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(seatReservations,
                linkTo(methodOn(SeatReservationController.class).allFromReservation(screeningId)).withSelfRel());
    }

    //ADD

    @PostMapping(path="/seat_reservations", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Resource<SeatReservation>> newSeatReservation(@RequestBody SeatReservationRequest request) {
        Screening screening = screeningRepository.findById(request.getScreeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(request.getScreeningId()));
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException(request.getReservationId()));

        SeatReservation seatReservation = new SeatReservation(screening, reservation,
                request.getTicketType(), request.getRow(),
                request.getColumn());
        SeatReservation newSeatReservation = repository.save(seatReservation);
        return ResponseEntity
                .created(linkTo(methodOn(SeatReservationController.class).one(newSeatReservation.getId())).toUri())
                .body(assembler.toResource(newSeatReservation));
    }

    //DELETE

    @DeleteMapping(path="/seat_reservations/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity <?> deleteSeatReservation(@PathVariable Long id) {
        return repository.findById(id).map(p -> {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new SeatReservationNotFoundException(id));
    }
}