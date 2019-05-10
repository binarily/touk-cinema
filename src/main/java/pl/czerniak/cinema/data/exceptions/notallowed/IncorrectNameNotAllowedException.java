package pl.czerniak.cinema.data.exceptions.notallowed;

public class IncorrectNameNotAllowedException extends NotAllowedException {
    public IncorrectNameNotAllowedException(){
        super("The name or surname provided is incorrect.");
    }
}
