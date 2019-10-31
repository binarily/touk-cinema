package pl.czerniak.cinema.data.exceptions.notallowed;

public class DuplicateSeatsNotAllowedException extends NotAllowedException {
    public DuplicateSeatsNotAllowedException() {
        super("You attempted to book seats that were already booked or to book the same seat twice.");
    }
}
