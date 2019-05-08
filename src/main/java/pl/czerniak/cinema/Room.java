package pl.czerniak.cinema;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
class Room {
    private @Id @GeneratedValue Long Id;
    private String Name = "";
    private Integer Rows = 0;
    private Integer SeatsInARow = 0;

    Room(String name, Integer rows, Integer seatsInARow){
        this.Name = name;
        this.Rows = rows;
        this.SeatsInARow = seatsInARow;
    }
    Room(){

    }
}
