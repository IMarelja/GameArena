package hr.algebra.gamearena.webapp.models.cereal.match;

import com.gamearena.client.model.GamesView;
import com.gamearena.client.model.MatchDetailedFullView;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.models.cereal.games.GamesViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.user.UserJustUsernameDecereal;

import java.time.OffsetDateTime;
import java.util.List;
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
    public static MatchDetailFullViewDecereal fromMatchDetailFullViewClient(MatchDetailedFullView match, GamesView game) throws NotFoundException {
        if (match.getPlayer1() == null || match.getPlayer2() == null) {
            throw new NotFoundException(List.of("Match with id: " + match.getId() + " is missing player information"));
        }

        return new MatchDetailFullViewDecereal(
                match.getId(),
                match.getTournamentId(),
                GamesViewDecereal.fromGamesViewClient(game),
                UserJustUsernameDecereal.fromUserJustUsernameClient(match.getPlayer1()),
                UserJustUsernameDecereal.fromUserJustUsernameClient(match.getPlayer2()),
                match.getPlayerOneScore(),
                match.getPlayerTwoScore(),
                Optional.ofNullable(match.getWinner()).map(UserJustUsernameDecereal::fromUserJustUsernameClient),
                match.getStatus() != null ? MatchStatusDecereal.fromMatchStatusClient(match.getStatus()) : null,
                match.getScheduledAt(),
                match.getPlayedAt(),
                match.getCreatedAt()
        );
    }
}
