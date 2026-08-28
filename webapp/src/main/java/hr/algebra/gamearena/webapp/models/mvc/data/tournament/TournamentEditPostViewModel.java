package hr.algebra.gamearena.webapp.models.mvc.data.tournament;

import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentEditCereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentFullViewDecereal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record TournamentEditPostViewModel(
        @NotBlank(message = "Name is required")
        String name,

        String description,

        @NotNull(message = "A game must be selected")
        Long gameId,

        @NotNull(message = "Status is required")
        TournamentStatusViewEnum status,

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
    public static TournamentEditPostViewModel fromTournamentFullViewDecereal(TournamentFullViewDecereal tournament) {
        return new TournamentEditPostViewModel(
                tournament.name(),
                tournament.description(),
                tournament.games() != null ? tournament.games().id() : null,
                tournament.status() != null ? TournamentStatusViewEnum.fromDecereal(tournament.status()) : null,
                tournament.soloPrice(),
                tournament.groupPrice(),
                tournament.currency() != null ? AcceptedCurrencyDataView.valueOf(tournament.currency()) : null,
                tournament.startsAt() != null ? tournament.startsAt().toLocalDateTime() : null,
                tournament.endsAt() != null ? tournament.endsAt().toLocalDateTime() : null
        );
    }

    public TournamentEditCereal toTournamentEditCereal() {
        return new TournamentEditCereal(
                name,
                description,
                gameId,
                status,
                soloPrice,
                groupPrice,
                currency,
                startsAt != null ? startsAt.atOffset(ZoneOffset.UTC) : null,
                endsAt != null ? endsAt.atOffset(ZoneOffset.UTC) : null
        );
    }
}
