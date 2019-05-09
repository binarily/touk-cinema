package pl.czerniak.cinema.data.requests;

import lombok.Data;
import pl.czerniak.cinema.data.objects.TicketType;

@Data
public class ReservationRequest {
    private String Name;
    private String Surname;
    private Long ScreeningId;
    private Seat[] Seats;
}

