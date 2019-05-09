package pl.czerniak.cinema.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czerniak.cinema.data.objects.Screening;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

}