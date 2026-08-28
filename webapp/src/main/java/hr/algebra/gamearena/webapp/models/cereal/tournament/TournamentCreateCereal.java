package hr.algebra.gamearena.webapp.models.cereal.tournament;

import hr.algebra.gamearena.webapp.models.mvc.data.tournament.AcceptedCurrencyDataView;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TournamentCreateCereal(
        String name,
        String description,
        Long gameId,
        BigDecimal soloPrice,
        BigDecimal groupPrice,
        AcceptedCurrencyDataView currency,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt
) {
}
