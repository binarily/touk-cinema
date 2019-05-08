package pl.czerniak.cinema;

import org.springframework.data.jpa.repository.JpaRepository;

interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {

}