package pl.czerniak.cinema;

import org.springframework.data.jpa.repository.JpaRepository;

interface ScreeningRepository extends JpaRepository<Screening, Long> {

}