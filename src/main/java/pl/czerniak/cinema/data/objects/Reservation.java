package pl.czerniak.cinema.data.objects;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class Reservation implements Comparable<Reservation>{
    private @Id @GeneratedValue Long id;
    private String name;
    private String surname;
    private LocalDateTime expiryDate;
    private Double totalPrice = 0.0;

    public Reservation(String name, String surname, LocalDateTime expiryDate){
        this.name = name;
        this.surname = surname;
        this.expiryDate = expiryDate;
    }

    public Reservation(){

    }

    public void addToTotalPrice(Double price){
        totalPrice += price;
    }

    @Override
    public int compareTo(Reservation o) {
        int dateCompare = this.getExpiryDate().compareTo(o.getExpiryDate());
        if(dateCompare == 0){
            int surnameCompare = this.getSurname().compareTo(o.getSurname());
            if(surnameCompare == 0){
                int nameCompare = this.getName().compareTo(o.getName());
                if(nameCompare == 0){
                    return this.getId().compareTo(o.getId());
                }
                return nameCompare;
            }
            return surnameCompare;
        }
        return dateCompare;
    }
}
