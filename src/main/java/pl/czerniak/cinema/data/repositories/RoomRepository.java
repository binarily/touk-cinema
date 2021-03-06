package pl.czerniak.cinema.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czerniak.cinema.data.objects.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}