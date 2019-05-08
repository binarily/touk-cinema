package pl.czerniak.cinema;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
class Film {
    public @Id @GeneratedValue Long Id;
    public String Title = "";

    Film(String title){
        this.Title = title;
    }

    Film(){

    }
}
