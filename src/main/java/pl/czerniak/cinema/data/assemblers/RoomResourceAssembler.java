package pl.czerniak.cinema.data.assemblers;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import pl.czerniak.cinema.data.controllers.RoomController;
import pl.czerniak.cinema.data.objects.Room;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class RoomResourceAssembler implements ResourceAssembler<Room, Resource<Room>> {

    @Override
    public Resource<Room> toResource(Room room) {

        return new Resource<>(room,
                linkTo(methodOn(RoomController.class).one(room.getId())).withSelfRel(),
                linkTo(methodOn(RoomController.class).all()).withRel("rooms"));
    }
}