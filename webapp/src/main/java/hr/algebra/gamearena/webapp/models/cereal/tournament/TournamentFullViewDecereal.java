package hr.algebra.gamearena.webapp.models.cereal.tournament;

import com.gamearena.client.model.TournamentFullView;
import hr.algebra.gamearena.webapp.models.cereal.games.GamesViewDecereal;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TournamentFullViewDecereal(
        Long id,
        String name,
        String description,
        TournamentStatusDecereal status,
        GamesViewDecereal games,
        BigDecimal soloPrice,
        BigDecimal groupPrice,
        String currency,
        OffsetDateTime startsAt,
        OffsetDateTime endsAt,
        OffsetDateTime createdAt
) {
    public static TournamentFullViewDecereal fromTournamentFullViewClient(TournamentFullView tournament) {
        return new TournamentFullViewDecereal(
                tournament.getId(),
                tournament.getName(),
                tournament.getDescription(),
                TournamentStatusDecereal.fromTournamentStatusClient(tournament.getStatus()),
                GamesViewDecereal.fromGamesViewClientOrUnavailableGarbage(tournament.getGames()),
                tournament.getSoloPrice(),
                tournament.getGroupPrice(),
                tournament.getCurrency(),
                tournament.getStartsAt(),
                tournament.getEndsAt(),
                tournament.getCreatedAt()
        );
    }
}
