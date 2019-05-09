package pl.czerniak.cinema.data.requests;

import lombok.Data;

@Data
public class SeatReservationRequest {
    private Long screeningId;
    private Long reservationId;
    private pl.czerniak.cinema.data.objects.TicketType TicketType;
    private Long row;
    private Long column;
}
