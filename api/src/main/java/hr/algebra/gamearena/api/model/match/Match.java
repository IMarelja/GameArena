package hr.algebra.gamearena.api.model.match;

import hr.algebra.gamearena.api.orm.postgres.match.MatchPostgres;

import java.time.LocalDateTime;

public record Match(
        Long id,
        Long tournamentId,
        Long gameId,
        Long playerOneId,
        Long playerTwoId,
        Integer playerOneScore,
        Integer playerTwoScore,
        Long winnerId,
        MatchStatus status,
        LocalDateTime scheduledAt,
        LocalDateTime playedAt,
        LocalDateTime createdAt
) {
    public static Match fromMatchPostgres(MatchPostgres match) {
        return new Match(
                match.getId(),
                match.getTournamentId(),
                match.getGameId(),
                match.getPlayerOneId(),
                match.getPlayerTwoId(),
                match.getPlayerOneScore(),
                match.getPlayerTwoScore(),
                match.getWinnerId(),
                MatchStatus.valueOf(match.getStatus()),
                match.getScheduledAt() != null ? match.getScheduledAt().toLocalDateTime() : null,
                match.getPlayedAt() != null ? match.getPlayedAt().toLocalDateTime() : null,
                match.getCreatedAt().toLocalDateTime()
        );
    }
}
