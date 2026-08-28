package hr.algebra.gamearena.webapp.models.mvc.data.match;

import hr.algebra.gamearena.webapp.models.cereal.match.MatchDetailFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchEditCereal;
import hr.algebra.gamearena.webapp.models.cereal.user.UserJustUsernameDecereal;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record MatchEditPostViewModel(
        @NotNull(message = "Player one is required")
        Long playerOneId,

        @NotNull(message = "Player two is required")
        Long playerTwoId,

        Integer playerOneScore,

        Integer playerTwoScore,

        Long winnerId,

        @NotNull(message = "Status is required")
        MatchStatusViewEnum status,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime scheduledAt,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime playedAt
) {
    public static MatchEditPostViewModel fromMatchDetailFullViewDecereal(MatchDetailFullViewDecereal match) {
        return new MatchEditPostViewModel(
                match.player1().id(),
                match.player2().id(),
                match.playerOneScore(),
                match.playerTwoScore(),
                match.winner().map(UserJustUsernameDecereal::id).orElse(null),
                match.status() != null ? MatchStatusViewEnum.fromDecereal(match.status()) : null,
                match.scheduledAt() != null ? match.scheduledAt().toLocalDateTime() : null,
                match.playedAt() != null ? match.playedAt().toLocalDateTime() : null
        );
    }

    public MatchEditCereal toMatchEditCereal() {
        return new MatchEditCereal(
                playerOneId,
                playerTwoId,
                playerOneScore,
                playerTwoScore,
                winnerId,
                status,
                scheduledAt != null ? scheduledAt.atOffset(ZoneOffset.UTC) : null,
                playedAt != null ? playedAt.atOffset(ZoneOffset.UTC) : null
        );
    }
}
