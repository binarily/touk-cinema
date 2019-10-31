package pl.czerniak.cinema.data.exceptions.notallowed;

public class SeatsOutOfBoundsNotAllowedException extends NotAllowedException {
    public SeatsOutOfBoundsNotAllowedException(){
        super("You tried to book a seat that doesn't fit in the room.");
    }
}
