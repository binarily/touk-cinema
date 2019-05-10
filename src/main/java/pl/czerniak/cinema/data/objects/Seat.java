package pl.czerniak.cinema.data.objects;

import lombok.Data;
import pl.czerniak.cinema.data.objects.TicketType;

@Data
public class Seat{
    private Long Row;
    private Long Column;
    private TicketType TicketType;
}
