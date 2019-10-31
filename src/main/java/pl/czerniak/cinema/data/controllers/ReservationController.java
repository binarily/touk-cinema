package pl.czerniak.cinema.data.controllers;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.czerniak.cinema.data.assemblers.ReservationResourceAssembler;
import pl.czerniak.cinema.data.exceptions.notallowed.*;
import pl.czerniak.cinema.data.exceptions.notfound.ReservationNotFoundException;
import pl.czerniak.cinema.data.exceptions.notfound.ScreeningNotFoundException;
import pl.czerniak.cinema.data.objects.Reservation;
import pl.czerniak.cinema.data.objects.Screening;
import pl.czerniak.cinema.data.objects.SeatReservation;
import pl.czerniak.cinema.data.objects.other.Seat;
import pl.czerniak.cinema.data.repositories.ReservationRepository;
import pl.czerniak.cinema.data.repositories.ScreeningRepository;
import pl.czerniak.cinema.data.repositories.SeatReservationRepository;
import pl.czerniak.cinema.data.requests.ReservationRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class ReservationController {

    private final ReservationRepository repository;
    private final ScreeningRepository screeningRepository;
    private final SeatReservationRepository seatReservationRepository;
    private final SeatReservationController seatReservationController;
    private final ReservationResourceAssembler assembler;

    ReservationController(ReservationRepository repository, ScreeningRepository screeningRepository,
                          SeatReservationRepository seatReservationRepository, SeatReservationController seatReservationController,
                          ReservationResourceAssembler assembler) {
        this.repository = repository;
        this.screeningRepository = screeningRepository;
        this.seatReservationRepository = seatReservationRepository;
        this.seatReservationController = seatReservationController;
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
                        .orElseThrow(() -> new ReservationNotFoundException(id)));
    }

    //ADD

    @PostMapping(path = "/reservations", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Resource<Reservation>> newReservation(@RequestBody ReservationRequest request) {
        Screening screening = screeningRepository.findById(request.getScreeningId())
                .orElseThrow(() -> new ScreeningNotFoundException(request.getScreeningId()));
        //check if more than 15 minutes to screening start
        if(screening.getStartDate().isBefore(LocalDateTime.now().plusMinutes(15))){
            throw new TimeNotAllowedException();
        }
        //check if reservation reserves at least a single seat
        if(request.getSeats() == null || request.getSeats().length == 0){
            throw new EmptyReservationNotAllowedException();
        }
        //check if name and surname match pattern
        String namePattern = "([A-ZÓĄŚŁŻŹĆŃ])([a-zóąśłżźćń]){2,}";
        String surnamePattern = "([A-ZĘÓĄŚŁŻŹĆŃ])([a-zęóąśłżźćń])*(([-])([A-ZĘÓĄŚŁŻŹĆŃ])([a-zęóąśłżźćń])*)?";
        if(!request.getName().matches(namePattern)||!request.getSurname().matches(surnamePattern) || request.getSurname().length() < 3){
            throw new IncorrectNameNotAllowedException();
        }

        Reservation reservation = new Reservation(request.getName(), request.getSurname(),
                screening.getStartDate().minusMinutes(15));
        ArrayList<SeatReservation> seatReservations = new ArrayList<>();
        for(Seat seat: request.getSeats()){
            //check if seats fit in the room
            if(Math.min(screening.getRoom().getRows(), Math.max(0, seat.getRow())) != seat.getRow() ||
                Math.min(screening.getRoom().getColumns(), Math.max(0, seat.getColumn())) != seat.getColumn()){
                throw new SeatsOutOfBoundsNotAllowedException();
            }

            SeatReservation seatReservation = new SeatReservation(screening, reservation,
                    seat.getTicketType(),seat.getRow(), seat.getColumn());
            seatReservations.add(seatReservation);
            reservation.addToTotalPrice(seat.getTicketType().getPrice());
        }
        List<SeatReservation> seatReservationsAfter = seatReservationRepository.findAllByScreeningEquals(screening);
        seatReservationsAfter.addAll(seatReservations);
        Collections.sort(seatReservationsAfter);
        for(int i=1; i < seatReservationsAfter.size();i++){
            SeatReservation seatReservation1 = seatReservationsAfter.get(i-1);
            SeatReservation seatReservation2 = seatReservationsAfter.get(i);
            //check if seats don't have a single seat between each other
            if(seatReservation1.getSeatRow().equals(seatReservation2.getSeatRow()) &&
                Math.abs(seatReservation1.getSeatColumn() - seatReservation2.getSeatColumn()) == 2){
                throw new SingleSeatBetweenNotAllowedException();
            }
            //check if seats are empty
            if(seatReservation1.getSeatRow().equals(seatReservation2.getSeatRow()) &&
                    seatReservation1.getSeatColumn().equals(seatReservation2.getSeatColumn())){
                throw new DuplicateSeatsNotAllowedException();
            }
        }
        Reservation newReservation = repository.save(reservation);
        seatReservationRepository.saveAll(seatReservations);

        return ResponseEntity
                .created(linkTo(methodOn(ReservationController.class).one(newReservation.getId())).toUri())
                .body(assembler.toResource(newReservation));
    }

    @PostMapping(path = "/screenings/{screeningId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Resource<Reservation>> newReservationFromScreening(@PathVariable Long screeningId,
                                                                             @RequestBody ReservationRequest request){
        request.setScreeningId(screeningId);
        return newReservation(request);
    }
    //DELETE
    @DeleteMapping(path = "/reservations/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity <?> deleteReservation(@PathVariable Long id) {
        return repository.findById(id).map(p -> {
            //remove all related seat reservations
            List<SeatReservation> seatReservations = seatReservationRepository.findAllByReservationEquals(p);
            for(SeatReservation sr : seatReservations){
                seatReservationController.deleteSeatReservation(sr.getId());
            }
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new ReservationNotFoundException(id));
    }
}