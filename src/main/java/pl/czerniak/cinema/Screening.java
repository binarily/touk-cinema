package pl.czerniak.cinema;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
class Screening {
    private @Id @GeneratedValue Long Id;
    // Screening of a...
    @ManyToOne
    private Film Film;
    // Screening in...
    @ManyToOne
    private Room Room;
    // Screening at...
    private Date Date;

    Screening(Film film, Room room, Date date){
        this.Film = film;
        this.Room = room;
        this.Date = date;
    }
}
