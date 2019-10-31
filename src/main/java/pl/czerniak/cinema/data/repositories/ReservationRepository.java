package pl.czerniak.cinema.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czerniak.cinema.data.objects.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}