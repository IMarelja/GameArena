package hr.algebra.gamearena.webapp.models.cereal.match;

import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchStatusViewEnum;

import java.time.OffsetDateTime;

public record MatchEditCereal(
        Long playerOneId,
        Long playerTwoId,
        Integer playerOneScore,
        Integer playerTwoScore,
        Long winnerId,
        MatchStatusViewEnum status,
        OffsetDateTime scheduledAt,
        OffsetDateTime playedAt
) {
}
