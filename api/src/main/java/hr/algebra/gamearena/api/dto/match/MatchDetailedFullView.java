package hr.algebra.gamearena.api.dto.match;

import hr.algebra.gamearena.api.dto.games.GamesView;
import hr.algebra.gamearena.api.dto.user.UserJustUsernameView;
import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.model.match.Match;
import hr.algebra.gamearena.api.model.user.User;

import java.time.LocalDateTime;
import java.util.Optional;

public record MatchDetailedFullView (
        Long id,
        Long tournamentId,
        GamesView game,
        UserJustUsernameView player1,
        UserJustUsernameView player2,
        Integer playerOneScore,
        Integer playerTwoScore,
        Optional<UserJustUsernameView> winner,
        MatchStatusView status,
        LocalDateTime scheduledAt,
        LocalDateTime playedAt,
        LocalDateTime createdAt
){
    public static MatchDetailedFullView fromMatchUserOneUserTwoAndGame(Match match, User playerOne, User playerTwo, Games game) {
        User winner = null;
        if (match.winnerId() != null) {
            winner = match.winnerId().equals(playerOne.id()) ? playerOne : playerTwo;
        }

        return new MatchDetailedFullView(
                match.id(),
                match.tournamentId(),
                GamesView.fromGamesModelOrNotFound(game),
                UserJustUsernameView.fromUser(playerOne),
                UserJustUsernameView.fromUser(playerTwo),
                match.playerOneScore(),
                match.playerTwoScore(),
                Optional.ofNullable(winner).map(UserJustUsernameView::fromUser),
                MatchStatusView.fromMatchStatus(match.status()),
                match.scheduledAt(),
                match.playedAt(),
                match.createdAt()
        );
    }
}
