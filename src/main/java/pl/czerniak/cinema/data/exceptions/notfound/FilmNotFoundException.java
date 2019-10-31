package pl.czerniak.cinema.data.exceptions.notfound;

public class FilmNotFoundException extends NotFoundException {
    public FilmNotFoundException(Long id){
        super("film", id);
    }
}
