package pl.czerniak.cinema.data.objects;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Room {
    private @Id @GeneratedValue Long Id;
    private String Name = "";
    private Integer Rows = 0;
    private Integer SeatsInARow = 0;

    public Room(String name, Integer rows, Integer seatsInARow){
        this.Name = name;
        this.Rows = rows;
        this.SeatsInARow = seatsInARow;
    }
    public Room(){

    }
}
