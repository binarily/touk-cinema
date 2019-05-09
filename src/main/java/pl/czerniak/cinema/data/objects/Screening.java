package pl.czerniak.cinema.data.objects;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
public class Screening {
    private @Id @GeneratedValue Long Id;
    // Screening of a...
    @ManyToOne
    private Film Film;
    // Screening in...
    @ManyToOne
    private Room Room;
    // Screening at...
    private LocalDateTime Date;

    public Screening(Film film, Room room, LocalDateTime date){
        this.Film = film;
        this.Room = room;
        this.Date = date;
    }
    public Screening(){

    }
}
