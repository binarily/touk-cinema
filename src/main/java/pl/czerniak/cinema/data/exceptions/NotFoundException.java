package pl.czerniak.cinema.data.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String objectName, Long id) {
        super(String.format("Could not find film %s %d", objectName, id));
    }
}