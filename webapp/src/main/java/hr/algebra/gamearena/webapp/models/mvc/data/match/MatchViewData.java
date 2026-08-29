package hr.algebra.gamearena.webapp.models.mvc.data.match;

import hr.algebra.gamearena.webapp.models.cereal.match.MatchDetailFullViewDecereal;
import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.user.UserJustUsernameViewData;

import java.time.OffsetDateTime;
import java.util.Optional;

public record MatchViewData(
        Long id,
        Long tournamentId,
        GameViewData game,
        UserJustUsernameViewData player1,
        UserJustUsernameViewData player2,
        Integer playerOneScore,
        Integer playerTwoScore,
        Optional<UserJustUsernameViewData> winner,
        MatchStatusViewEnum status,
        OffsetDateTime scheduledAt,
        OffsetDateTime playedAt,
        OffsetDateTime createdAt
) {
    public static MatchViewData fromMatchDetailFullViewDecereal(MatchDetailFullViewDecereal match) {
        return new MatchViewData(
                match.id(),
                match.tournamentId(),
                GameViewData.fromGamesViewDecereal(match.game()),
                UserJustUsernameViewData.from(match.player1()),
                UserJustUsernameViewData.from(match.player2()),
                match.playerOneScore(),
                match.playerTwoScore(),
                match.winner().map(UserJustUsernameViewData::from),
                match.status() != null ? MatchStatusViewEnum.fromDecereal(match.status()) : null,
                match.scheduledAt(),
                match.playedAt(),
                match.createdAt()
        );
    }
}
