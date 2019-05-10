package pl.czerniak.cinema.data.objects;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Screening {
    private @Id @GeneratedValue Long id;
    // Screening of a...
    @ManyToOne
    private Film film;
    // Screening in...
    @ManyToOne
    private Room room;
    // Screening at...
    private LocalDateTime startDate;

    public Screening(Film film, Room room, LocalDateTime startDate){
        this.film = film;
        this.room = room;
        this.startDate = startDate;
    }
    public Screening(){

    }
}
