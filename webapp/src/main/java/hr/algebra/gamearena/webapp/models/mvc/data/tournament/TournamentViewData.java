package hr.algebra.gamearena.webapp.models.mvc.data.tournament;

import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentFullViewDecereal;
import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewData;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TournamentViewData(
        Long id,
        String name,
        String description,
        TournamentStatusViewEnum status,
        GameViewData games,
        BigDecimal soloPrice,
        BigDecimal groupPrice,
        String currency,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt,
        OffsetDateTime createdAt
) {
    public static TournamentViewData fromTournamentFullViewDecereal(TournamentFullViewDecereal tournament) {
        return new TournamentViewData(
                tournament.id(),
                tournament.name(),
                tournament.description(),
                tournament.status() != null ? TournamentStatusViewEnum.fromDecereal(tournament.status()) : null,
                GameViewData.fromGamesViewDecereal(tournament.games()),
                tournament.soloPrice(),
                tournament.groupPrice(),
                tournament.currency(),
                tournament.startsAt(),
                tournament.endsAt(),
                tournament.createdAt()
        );
    }
}
