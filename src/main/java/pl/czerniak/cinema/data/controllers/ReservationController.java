package pl.czerniak.cinema.data.controllers;

import org.springframework.web.bind.annotation.*;
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

@RestController
class ReservationController {

    private final ReservationRepository repository;
    private final ScreeningRepository screeningRepository;
    private final SeatReservationRepository seatReservationRepository;

    ReservationController(ReservationRepository repository, ScreeningRepository screeningRepository,
                          SeatReservationRepository seatReservationRepository) {
        this.repository = repository;
        this.screeningRepository = screeningRepository;
        this.seatReservationRepository = seatReservationRepository;
    }

    //LIST

    @GetMapping("/reservations")
    List<Reservation> all() {
        return repository.findAll();
    }

    // Single item

    @GetMapping("/reservations/{id}")
    Reservation one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("reservation", id));
    }

    //ADD

    @PostMapping("/reservations")
    Reservation newReservation(@RequestBody ReservationRequest request) {
        Screening screening = screeningRepository.getOne(request.getScreeningId());

        Reservation newReservation = new Reservation(request.getName(), request.getSurname());
        for(Seat seat: request.getSeats()){
            SeatReservation seatReservation = new SeatReservation(screening, newReservation,
                    seat.getTicketType(),seat.getRow(), seat.getColumn());
            //TODO: check if seats are empty
            seatReservationRepository.save(seatReservation);
        }
        return repository.save(newReservation);
    }

    //REPLACE

    @PutMapping("/reservations/{id}")
    Reservation replaceReservation(@RequestBody ReservationRequest request, @PathVariable Long id) {

        return repository.findById(id)
                .map(reservation -> {
                    reservation.setName(request.getName());
                    reservation.setSurname(request.getSurname());
                    return repository.save(reservation);
                })
                .orElseGet(() -> newReservation(request));
    }

    //DELETE

    @DeleteMapping("/reservations/{id}")
    void deleteReservation(@PathVariable Long id) {
        repository.deleteById(id);
        //TODO: remove all related seat reservations
    }
}