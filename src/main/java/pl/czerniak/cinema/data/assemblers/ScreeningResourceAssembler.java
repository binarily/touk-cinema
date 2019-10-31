package pl.czerniak.cinema.data.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import pl.czerniak.cinema.data.controllers.FilmController;
import pl.czerniak.cinema.data.controllers.RoomController;
import pl.czerniak.cinema.data.controllers.ScreeningController;
import pl.czerniak.cinema.data.controllers.SeatReservationController;
import pl.czerniak.cinema.data.objects.Screening;

@Component
public class ScreeningResourceAssembler implements ResourceAssembler<Screening, Resource<Screening>> {

    @Override
    public Resource<Screening> toResource(Screening screening) {

        return new Resource<>(screening,
                linkTo(methodOn(ScreeningController.class).one(screening.getId())).withSelfRel(),
                linkTo(methodOn(ScreeningController.class).all()).withRel("screenings"),
                linkTo(methodOn(FilmController.class).one(screening.getFilm().getId())).withRel("film"),
                linkTo(methodOn(RoomController.class).one(screening.getRoom().getId())).withRel("room"),
                linkTo(methodOn(SeatReservationController.class).allFromScreening(screening.getId())).withRel("seats"));
    }
}