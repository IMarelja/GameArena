package hr.algebra.gamearena.api.model.match;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class MatchUpdate {
    private Long playerOneId;
    private Long playerTwoId;
    private Integer playerOneScore;
    private Integer playerTwoScore;
    private Long winnerId;
    private MatchStatus status;
    private OffsetDateTime scheduledAt;
    private OffsetDateTime playedAt;
}
