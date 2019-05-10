package pl.czerniak.cinema.data.exceptions;

public class ScreeningNotFoundException extends NotFoundException {
    public ScreeningNotFoundException(Long id){
        super("screening", id);
    }
}
