package pl.czerniak.cinema.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czerniak.cinema.data.objects.Film;
import pl.czerniak.cinema.data.objects.Room;
import pl.czerniak.cinema.data.objects.Screening;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findAllByStartDateAfter(LocalDateTime dateTime);
    List<Screening> findAllByFilmEquals(Film film);
    List<Screening> findAllByRoomEquals(Room room);
}