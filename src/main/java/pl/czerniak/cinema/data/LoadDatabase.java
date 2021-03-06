package pl.czerniak.cinema.data;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.czerniak.cinema.data.objects.Film;
import pl.czerniak.cinema.data.objects.Room;
import pl.czerniak.cinema.data.objects.Screening;
import pl.czerniak.cinema.data.repositories.FilmRepository;
import pl.czerniak.cinema.data.repositories.RoomRepository;
import pl.czerniak.cinema.data.repositories.ScreeningRepository;

import java.time.LocalDateTime;

@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    @Autowired
    CommandLineRunner initDatabase(FilmRepository filmRepository, RoomRepository roomRepository, ScreeningRepository screeningRepository) {
        return args -> {
            log.info("Preloading " + filmRepository.save(new Film("Breakin' 2: Electric Boogaloo")));
            log.info("Preloading " + filmRepository.save(new Film("No Idea For A Title")));
            log.info("Preloading " + filmRepository.save(new Film("No Idea For A Title 2: Electric Boogaloo")));
            filmRepository.flush();

            log.info("Preloading " + roomRepository.save(new Room("1", 5, 10)));
            log.info("Preloading " + roomRepository.save(new Room("2", 10, 20)));
            log.info("Preloading " + roomRepository.save(new Room("3", 10, 10)));
            roomRepository.flush();


            log.info("Preloading " + screeningRepository.save(new Screening(filmRepository.findAll().get(0),
                    roomRepository.findAll().get(0), LocalDateTime.now())));
            log.info("Preloading " + screeningRepository.save(new Screening(filmRepository.findAll().get(2),
                    roomRepository.findAll().get(1), LocalDateTime.now().plusMinutes(16))));
            log.info("Preloading " + screeningRepository.save(new Screening(filmRepository.findAll().get(1),
                    roomRepository.findAll().get(2), LocalDateTime.now().plusMinutes(17))));
            log.info("Preloading " + screeningRepository.save(new Screening(filmRepository.findAll().get(1),
                    roomRepository.findAll().get(0), LocalDateTime.now().plusMinutes(21))));
            log.info("Preloading " + screeningRepository.save(new Screening(filmRepository.findAll().get(2),
                    roomRepository.findAll().get(1), LocalDateTime.now().plusMinutes(15).plusHours(1))));
            log.info("Preloading " + screeningRepository.save(new Screening(filmRepository.findAll().get(0),
                    roomRepository.findAll().get(2), LocalDateTime.now().plusMinutes(15).minusDays(1))));
            screeningRepository.flush();
        };
    }
}