package pl.czerniak.cinema.data.objects;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Film implements Comparable<Film>{
    private @Id @GeneratedValue Long id;
    private String title = "";

    public Film(String title){
        this.title = title;
    }

    public Film(){

    }

    @Override
    public int compareTo(Film o) {
        int titleCompare = this.getTitle().compareTo(o.getTitle());
        if(titleCompare == 0){
            return this.getId().compareTo(o.getId());
        }
        return titleCompare;
    }
}
