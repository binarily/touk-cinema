package pl.czerniak.cinema;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.*;

@Data
@Entity
class Reservation {
    private @Id @GeneratedValue Long Id;
    private String Name;
    private String Surname;

    Reservation(String name, String surname){
        this.Name = name;
        this.Surname = surname;
    }
}
