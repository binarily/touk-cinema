package pl.czerniak.cinema.data.exceptions.notfound;

public class SeatReservationNotFoundException extends NotFoundException {
    public SeatReservationNotFoundException(Long id){
        super("seat reservation", id);
    }
}
