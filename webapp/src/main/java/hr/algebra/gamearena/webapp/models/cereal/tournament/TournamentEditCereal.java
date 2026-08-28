package hr.algebra.gamearena.webapp.models.cereal.tournament;

import hr.algebra.gamearena.webapp.models.mvc.data.tournament.AcceptedCurrencyDataView;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentStatusViewEnum;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TournamentEditCereal(
        String name,
        String description,
        Long gameId,
        TournamentStatusViewEnum status,
        BigDecimal soloPrice,
        BigDecimal groupPrice,
        AcceptedCurrencyDataView currency,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt
) {
}
