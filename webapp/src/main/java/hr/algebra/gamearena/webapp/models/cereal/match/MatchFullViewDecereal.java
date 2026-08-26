package hr.algebra.gamearena.webapp.models.cereal.match;

import com.gamearena.client.model.MatchFullView;

import java.time.OffsetDateTime;

public record MatchFullViewDecereal(
        Long id,
        Long tournamentId,
        Long gameId,
        Long playerOneId,
        Long playerTwoId,
        Integer playerOneScore,
        Integer playerTwoScore,
        Long winnerId,
        String status,
        OffsetDateTime scheduledAt,
        OffsetDateTime playedAt,
        OffsetDateTime createdAt
) {
    public static MatchFullViewDecereal fromMatchFullViewClient(MatchFullView match) {
        return new MatchFullViewDecereal(
                match.getId(),
                match.getTournamentId(),
                match.getGameId(),
                match.getPlayerOneId(),
                match.getPlayerTwoId(),
                match.getPlayerOneScore(),
                match.getPlayerTwoScore(),
                match.getWinnerId(),
                match.getStatus() != null ? match.getStatus().getValue() : null,
                match.getScheduledAt(),
                match.getPlayedAt(),
                match.getCreatedAt()
        );
    }
}
