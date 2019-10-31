package pl.czerniak.cinema.data.objects.other;

import lombok.Data;

@Data
public class Seat{
    private Long row;
    private Long column;
    private TicketType ticketType;
}
