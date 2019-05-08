package pl.czerniak.cinema;

import org.springframework.data.jpa.repository.JpaRepository;

interface FilmRepository extends JpaRepository<Film, Long> {

}