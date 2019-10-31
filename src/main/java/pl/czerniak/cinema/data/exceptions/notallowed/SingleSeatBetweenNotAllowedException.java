package pl.czerniak.cinema.data.exceptions.notallowed;

public class SingleSeatBetweenNotAllowedException extends NotAllowedException {
    public SingleSeatBetweenNotAllowedException(){
        super("You tried to book seats in a way that would leave one seat between other booked seats empty.");
    }
}
