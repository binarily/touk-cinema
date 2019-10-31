package pl.czerniak.cinema.data.exceptions.notfound;

public class NotFoundException extends RuntimeException {

    NotFoundException(String objectName, Long id) {
        super(String.format("Could not find %s %d", objectName, id));
    }
}