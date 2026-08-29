package hr.algebra.gamearena.webapp.models.cereal.match;

import com.gamearena.client.model.MatchDetailedFullView;
import hr.algebra.gamearena.webapp.models.cereal.games.GamesViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.user.UserJustUsernameDecereal;

import java.time.OffsetDateTime;
import java.util.Optional;

public record MatchDetailFullViewDecereal(
        Long id,
        Long tournamentId,
        GamesViewDecereal game,
        UserJustUsernameDecereal player1,
        UserJustUsernameDecereal player2,
        Integer playerOneScore,
        Integer playerTwoScore,
        Optional<UserJustUsernameDecereal> winner,
        MatchStatusDecereal status,
        OffsetDateTime scheduledAt,
        OffsetDateTime playedAt,
        OffsetDateTime createdAt
) {
    public static MatchDetailFullViewDecereal fromMatchDetailFullViewClient(MatchDetailedFullView match) {
        return new MatchDetailFullViewDecereal(
                match.getId(),
                match.getTournamentId(),
                GamesViewDecereal.fromGamesViewClientOrUnavailableGarbage(match.getGame()),
                UserJustUsernameDecereal.fromUserJustUsernameClientOrUnavailableGarbage(match.getPlayer1()),
                UserJustUsernameDecereal.fromUserJustUsernameClientOrUnavailableGarbage(match.getPlayer2()),
                match.getPlayerOneScore(),
                match.getPlayerTwoScore(),
                Optional.ofNullable(match.getWinner()).map(UserJustUsernameDecereal::fromUserJustUsernameClient),
                MatchStatusDecereal.fromMatchStatusClient(match.getStatus()),
                match.getScheduledAt(),
                match.getPlayedAt(),
                match.getCreatedAt()
        );
    }
}
