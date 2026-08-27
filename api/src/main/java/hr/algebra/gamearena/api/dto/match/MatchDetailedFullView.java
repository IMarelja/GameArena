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
    public static MatchDetailedFullView fromMatchUserOneUserTwoAndGame(Match match, Optional<User> playerOne, Optional<User> playerTwo, Optional<User> winner, Optional<Games> game) {


        return new MatchDetailedFullView(
                match.id(),
                match.tournamentId(),
                game.map(GamesView::fromGamesModel)
                        .orElse(GamesView.deletedGame()),
                playerOne.map(UserJustUsernameView::fromUser)
                        .orElse(UserJustUsernameView.deleteUser()),
                playerTwo.map(UserJustUsernameView::fromUser)
                        .orElse(UserJustUsernameView.deleteUser()),
                match.playerOneScore(),
                match.playerTwoScore(),
                winner.map(UserJustUsernameView::fromUser),
                MatchStatusView.fromMatchStatus(match.status()),
                match.scheduledAt(),
                match.playedAt(),
                match.createdAt()
        );
    }
}
