package pl.czerniak.cinema.data.objects;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
public class Reservation {
    private @Id @GeneratedValue Long Id;
    private String Name;
    private String Surname;
    private LocalDateTime ExpiryDate;

    public Reservation(String name, String surname){
        this.Name = name;
        this.Surname = surname;
        this.ExpiryDate = LocalDateTime.now().plusHours(1);
    }

    public Reservation(){

    }
}
