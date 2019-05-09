package pl.czerniak.cinema.data.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import pl.czerniak.cinema.data.controllers.FilmController;
import pl.czerniak.cinema.data.controllers.ReservationController;
import pl.czerniak.cinema.data.controllers.ScreeningController;
import pl.czerniak.cinema.data.controllers.SeatReservationController;
import pl.czerniak.cinema.data.objects.Film;
import pl.czerniak.cinema.data.objects.SeatReservation;

@Component
public class SeatReservationResourceAssembler implements ResourceAssembler<SeatReservation, Resource<SeatReservation>> {

    @Override
    public Resource<SeatReservation> toResource(SeatReservation seatReservation) {

        return new Resource<>(seatReservation,
                linkTo(methodOn(SeatReservationController.class).one(seatReservation.getId())).withSelfRel(),
                linkTo(methodOn(SeatReservationController.class).all()).withRel("seat_reservations"),
                linkTo(methodOn(ReservationController.class).one(seatReservation.getReservation().getId())).withRel("reservation"),
                linkTo(methodOn(ScreeningController.class).one(seatReservation.getScreening().getId())).withRel("screening"));
    }
}