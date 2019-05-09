package pl.czerniak.cinema.data.requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScreeningRequest {
    private Long FilmId;
    private Long RoomId;
    private LocalDateTime Date;
}
