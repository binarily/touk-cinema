package pl.czerniak.cinema.data.objects;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity
public class Screening implements Comparable<Screening>{
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

    @Override
    public int compareTo(Screening o) {
        int filmCompare = this.getFilm().compareTo(o.getFilm());
        if(filmCompare == 0) {
            int startDateCompare = this.getStartDate().compareTo(o.getStartDate());
            if(startDateCompare == 0) {
                int roomCompare = this.getRoom().compareTo(o.getRoom());
                if (roomCompare == 0) {
                    return this.getId().compareTo(o.getId());
                }
                return roomCompare;
            }
            return startDateCompare;
        }
        return filmCompare;
    }
}
