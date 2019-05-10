package pl.czerniak.cinema.data.exceptions.notfound;

public class ReservationNotFoundException extends NotFoundException {
    public ReservationNotFoundException(Long id){
        super("reservation", id);
    }
}
