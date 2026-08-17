package hr.algebra.gamearena.api.model.tournament;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TournamentSave {
    private String name;
    private String description;
    private Long gameId;
    private TournamentStatus status;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
}
