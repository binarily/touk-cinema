package pl.czerniak.cinema.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czerniak.cinema.data.objects.Film;

public interface FilmRepository extends JpaRepository<Film, Long> {

}