package pl.czerniak.cinema.data.objects;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Room implements Comparable<Room>{
    private @Id @GeneratedValue Long id;
    private String name = "";
    private Integer rows = 0;
    private Integer columns = 0;

    public Room(String name, Integer rows, Integer columns){
        this.name = name;
        this.rows = rows;
        this.columns = columns;
    }
    public Room(){

    }

    @Override
    public int compareTo(Room o) {
        int nameCompare = this.getName().compareTo(o.getName());
        if(nameCompare == 0){
            return this.getId().compareTo(o.getId());
        }
        return nameCompare;
    }
}
