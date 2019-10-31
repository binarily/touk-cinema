package pl.czerniak.cinema.data.exceptions.notallowed;

public class TimeNotAllowedException extends NotAllowedException {
    public TimeNotAllowedException(){
        super("reservation is made too late. Please buy tickets at the office.");
    }
}
