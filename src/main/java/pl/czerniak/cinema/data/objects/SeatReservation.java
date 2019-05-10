package pl.czerniak.cinema.data.objects;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class SeatReservation {
    private @Id @GeneratedValue Long id;
    @ManyToOne
    private Screening screening;
    @ManyToOne
    private Reservation reservation;
    private TicketType ticketType;
    private Long seatRow;
    private Long seatColumn;

    public SeatReservation(Screening screening, Reservation reservation, TicketType ticketType,
                           Long seatRow, Long seatColumn){
        this.screening = screening;
        this.reservation = reservation;
        this.ticketType = ticketType;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
    }

    public SeatReservation(){

    }

}

