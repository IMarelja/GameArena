package hr.algebra.gamearena.webapp.models.mvc.data.tournament;

import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentCreateCereal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record TournamentCreatePostViewModel(
        @NotBlank(message = "Name is required")
        String name,

        String description,

        @NotNull(message = "A game must be selected")
        Long gameId,

        @NotNull(message = "Solo price is required")
        BigDecimal soloPrice,

        @NotNull(message = "Group price is required")
        BigDecimal groupPrice,

        @NotNull(message = "Currency is required")
        AcceptedCurrencyDataView currency,

        @NotNull(message = "Start date is required")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime startsAt,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime endsAt
) {
    public TournamentCreateCereal toTournamentCreateCereal() {
        return new TournamentCreateCereal(
                name,
                description,
                gameId,
                soloPrice,
                groupPrice,
                currency,
                startsAt != null ? startsAt.atOffset(ZoneOffset.UTC) : null,
                endsAt != null ? endsAt.atOffset(ZoneOffset.UTC) : null
        );
    }
}
