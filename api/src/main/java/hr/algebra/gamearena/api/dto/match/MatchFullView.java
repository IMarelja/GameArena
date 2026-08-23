package hr.algebra.gamearena.api.dto.match;

import hr.algebra.gamearena.api.model.match.Match;

import java.time.LocalDateTime;

public record MatchFullView(
        Long id,
        Long tournamentId,
        Long gameId,
        Long playerOneId,
        Long playerTwoId,
        Integer playerOneScore,
        Integer playerTwoScore,
        Long winnerId,
        MatchStatusView status,
        LocalDateTime scheduledAt,
        LocalDateTime playedAt,
        LocalDateTime createdAt
) {
    public static MatchFullView fromMatch(Match match) {
        return new MatchFullView(
                match.id(),
                match.tournamentId(),
                match.gameId(),
                match.playerOneId(),
                match.playerTwoId(),
                match.playerOneScore(),
                match.playerTwoScore(),
                match.winnerId(),
                MatchStatusView.fromMatchStatus(match.status()),
                match.scheduledAt(),
                match.playedAt(),
                match.createdAt()
        );
    }
}
