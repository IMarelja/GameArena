package hr.algebra.gamearena.webapp.models.mvc.data.match;

import hr.algebra.gamearena.webapp.models.cereal.match.MatchCreateCereal;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record MatchCreatePostViewModel(
        @NotNull(message = "Player one is required")
        Long playerOneId,

        @NotNull(message = "Player two is required")
        Long playerTwoId,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime scheduledAt
) {
    public MatchCreateCereal toMatchCreateCereal(Long tournamentId) {
        return new MatchCreateCereal(
                tournamentId,
                playerOneId,
                playerTwoId,
                scheduledAt != null ? scheduledAt.atOffset(ZoneOffset.UTC) : null
        );
    }
}
