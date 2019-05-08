package pl.czerniak.cinema;

import org.springframework.data.jpa.repository.JpaRepository;

interface RoomRepository extends JpaRepository<Room, Long> {

}