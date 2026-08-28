package hr.algebra.gamearena.webapp.models.cereal.match;

import java.time.OffsetDateTime;

public record MatchCreateCereal(
        Long tournamentId,
        Long playerOneId,
        Long playerTwoId,
        OffsetDateTime scheduledAt
) {
}
