package pl.czerniak.cinema.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czerniak.cinema.data.objects.SeatReservation;

public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {

}