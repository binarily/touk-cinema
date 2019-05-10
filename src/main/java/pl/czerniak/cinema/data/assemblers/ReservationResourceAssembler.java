package pl.czerniak.cinema.data.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import pl.czerniak.cinema.data.controllers.ReservationController;
import pl.czerniak.cinema.data.controllers.SeatReservationController;
import pl.czerniak.cinema.data.objects.Reservation;

@Component
public class ReservationResourceAssembler implements ResourceAssembler<Reservation, Resource<Reservation>> {

    @Override
    public Resource<Reservation> toResource(Reservation reservation) {

        return new Resource<>(reservation,
                linkTo(methodOn(ReservationController.class).one(reservation.getId())).withSelfRel(),
                linkTo(methodOn(ReservationController.class).all()).withRel("reservations"),
                linkTo(methodOn(SeatReservationController.class).allFromReservation(reservation.getId())).withRel("seats"));
    }
}