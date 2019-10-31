package pl.czerniak.cinema.data.exceptions.notfound;

public class ScreeningNotFoundException extends NotFoundException {
    public ScreeningNotFoundException(Long id){
        super("screening", id);
    }
}
