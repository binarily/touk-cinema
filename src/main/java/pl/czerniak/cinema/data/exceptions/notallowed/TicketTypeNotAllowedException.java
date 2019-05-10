package pl.czerniak.cinema.data.exceptions.notallowed;

public class TicketTypeNotAllowedException extends NotAllowedException {
    public TicketTypeNotAllowedException(){
        super("Ticket type provided is not correct.");
    }
}
