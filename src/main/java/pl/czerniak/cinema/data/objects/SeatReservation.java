package pl.czerniak.cinema.data.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import pl.czerniak.cinema.data.objects.other.TicketType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class SeatReservation implements Comparable<SeatReservation> {
    private @Id @GeneratedValue Long id;
    @ManyToOne
    private Screening screening;
    @ManyToOne
    @JsonIgnore
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

    @Override
    public int compareTo(SeatReservation o) {
        int screeningCompare = this.getScreening().compareTo(o.getScreening());
        if(screeningCompare == 0){
            int rowCompare = this.getSeatRow().compareTo(o.getSeatRow());
            if(rowCompare == 0){
                return this.getSeatColumn().compareTo(o.getSeatColumn());
            }
            return rowCompare;
        }
        return screeningCompare;
    }

}

