package pl.czerniak.cinema.data.requests;

import lombok.Data;
import pl.czerniak.cinema.data.objects.TicketType;

@Data
public class Seat{
    private Long Row;
    private Long Column;
    private pl.czerniak.cinema.data.objects.TicketType TicketType;
}
