package pl.czerniak.cinema.data.exceptions.notallowed;

public class EmptyReservationNotAllowedException extends NotAllowedException {
    public EmptyReservationNotAllowedException(){
        super("You tried to book no seats.");
    }
}
