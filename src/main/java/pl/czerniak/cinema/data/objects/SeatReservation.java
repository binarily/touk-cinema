package pl.czerniak.cinema.data.objects;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class SeatReservation {
    private @Id @GeneratedValue Long Id;
    @ManyToOne
    private Screening Screening;
    @ManyToOne
    private Reservation Reservation;
    private TicketType TicketType;
    private Long SeatRow;
    private Long SeatColumn;

    public SeatReservation(Screening screening, Reservation reservation, TicketType ticketType,
                           Long seatRow, Long seatColumn){
        this.Screening = screening;
        this.Reservation = reservation;
        this.TicketType = ticketType;
        this.SeatRow = seatRow;
        this.SeatColumn = seatColumn;
    }

}

