package pl.czerniak.cinema.data.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import pl.czerniak.cinema.data.controllers.FilmController;
import pl.czerniak.cinema.data.objects.Film;

@Component
public class FilmResourceAssembler implements ResourceAssembler<Film, Resource<Film>> {

    @Override
    public Resource<Film> toResource(Film film) {

        return new Resource<>(film,
                linkTo(methodOn(FilmController.class).one(film.getId())).withSelfRel(),
                linkTo(methodOn(FilmController.class).all()).withRel("films"));
    }
}