package pl.czerniak.cinema.data.objects;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Film {
    private @Id @GeneratedValue Long Id;
    private String Title = "";

    public Film(String title){
        this.Title = title;
    }

    public Film(){

    }
}
