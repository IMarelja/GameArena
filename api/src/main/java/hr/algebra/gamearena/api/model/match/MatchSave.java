package hr.algebra.gamearena.api.model.match;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class MatchSave {
    private Long tournamentId;
    private Long gameId;
    private Long playerOneId;
    private Long playerTwoId;
    private MatchStatus status;
    private OffsetDateTime scheduledAt;
}
