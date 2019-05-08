package pl.czerniak.cinema;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
class SeatReservation {
    private @Id @GeneratedValue Long Id;
    @ManyToOne
    private Screening Screening;
    @ManyToOne
    private Reservation Reservation;
    private TicketType TicketType;
}

enum TicketType {ADULT, STUDENT, CHILD}