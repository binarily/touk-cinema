package pl.czerniak.cinema.data.requests;

import lombok.Data;
import pl.czerniak.cinema.data.objects.other.TicketType;

@Data
public class SeatReservationRequest {
    private Long screeningId;
    private Long reservationId;
    private TicketType ticketType;
    private Long row;
    private Long column;
}
