package hr.algebra.gamearena.api.model.tournament;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class TournamentQuery {
    Optional<Long> gameId;
    Boolean ascending;
}
