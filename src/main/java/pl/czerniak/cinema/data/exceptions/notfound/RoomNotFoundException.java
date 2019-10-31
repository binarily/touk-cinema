package pl.czerniak.cinema.data.exceptions.notfound;

public class RoomNotFoundException extends NotFoundException {
    public RoomNotFoundException(Long id){
        super("room", id);
    }
}
