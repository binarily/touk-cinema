package pl.czerniak.cinema.data.controllers;

import org.springframework.web.bind.annotation.*;
import pl.czerniak.cinema.data.exceptions.NotFoundException;
import pl.czerniak.cinema.data.objects.Reservation;
import pl.czerniak.cinema.data.objects.Screening;
import pl.czerniak.cinema.data.objects.SeatReservation;
import pl.czerniak.cinema.data.repositories.ReservationRepository;
import pl.czerniak.cinema.data.repositories.ScreeningRepository;
import pl.czerniak.cinema.data.repositories.SeatReservationRepository;
import pl.czerniak.cinema.data.requests.SeatReservationRequest;

import java.util.List;

@RestController
class SeatReservationController {

    private final SeatReservationRepository repository;
    private final ScreeningRepository screeningRepository;
    private final ReservationRepository reservationRepository;

    SeatReservationController(SeatReservationRepository repository,
                              ScreeningRepository screeningRepository,
                              ReservationRepository reservationRepository) {
        this.repository = repository;
        this.screeningRepository = screeningRepository;
        this.reservationRepository = reservationRepository;
    }

    //LIST

    @GetMapping("/seat_reservations")
    List<SeatReservation> all() {
        return repository.findAll();
    }

    // Single item

    @GetMapping("/seat_reservations/{id}")
    SeatReservation one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("seat reservation", id));
    }

    //ADD

    @PostMapping("/seat_reservations")
    SeatReservation newSeatReservation(@RequestBody SeatReservationRequest request) {
        Screening screening = screeningRepository.getOne(request.getScreeningId());
        Reservation reservation = reservationRepository.getOne(request.getReservationId());

        SeatReservation newSeatReservation = new SeatReservation(screening, reservation,
                request.getTicketType(), request.getRow(),
                request.getColumn());
        return repository.save(newSeatReservation);
    }

    //REPLACE

    @PutMapping("/seat_reservations/{id}")
    SeatReservation replaceReservation(@RequestBody SeatReservationRequest request,
                                       @PathVariable Long id) {
        Screening screening = screeningRepository.getOne(request.getScreeningId());
        Reservation reservation = reservationRepository.getOne(request.getReservationId());

        return repository.findById(id)
                .map(seatReservation -> {
                    seatReservation.setReservation(reservation);
                    seatReservation.setScreening(screening);
                    seatReservation.setTicketType(request.getTicketType());
                    seatReservation.setSeatRow(request.getRow());
                    seatReservation.setSeatColumn(request.getColumn());
                    return repository.save(seatReservation);
                })
                .orElseGet(() -> newSeatReservation(request));
    }

    //DELETE

    @DeleteMapping("/seat_reservations/{id}")
    void deleteReservation(@PathVariable Long id) {
        repository.deleteById(id);
    }
}