package hr.algebra.gamearena.api.dto.match;

import hr.algebra.gamearena.api.dto.user.UserJustUsernameView;
import hr.algebra.gamearena.api.model.match.Match;
import hr.algebra.gamearena.api.model.user.User;

import java.time.LocalDateTime;
import java.util.Optional;

public record MatchDetailedFullView (
        Long id,
        Long tournamentId,
        Long gameId,
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
    public static MatchDetailedFullView fromMatchUserOneUserTwo(Match match, User playerOne, User playerTwo) {
        User winner = null;
        if (match.winnerId() != null) {
            winner = match.winnerId().equals(playerOne.id()) ? playerOne : playerTwo;
        }

        return new MatchDetailedFullView(
                match.id(),
                match.tournamentId(),
                match.gameId(),
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
