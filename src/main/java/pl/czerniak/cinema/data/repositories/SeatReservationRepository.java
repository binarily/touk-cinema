package pl.czerniak.cinema.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czerniak.cinema.data.objects.Reservation;
import pl.czerniak.cinema.data.objects.Screening;
import pl.czerniak.cinema.data.objects.SeatReservation;

import java.util.List;

public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {
    List<SeatReservation> findAllByReservationEquals(Reservation reservation);
    List<SeatReservation> findAllByScreeningEquals(Screening screening);
}