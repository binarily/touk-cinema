package pl.czerniak.cinema.data.requests;

import lombok.Data;
import pl.czerniak.cinema.data.objects.other.Seat;

@Data
public class ReservationRequest {
    private String name;
    private String surname;
    private Long screeningId;
    private Seat[] seats;
}

