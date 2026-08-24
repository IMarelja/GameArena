package hr.algebra.gamearena.webapp.models.cereal.tournament;

import com.gamearena.client.model.TournamentFullView;
import hr.algebra.gamearena.webapp.models.cereal.games.GamesViewDecereal;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TournamentFullViewDecereal(
        Long id,
        String name,
        String description,
        String status,
        GamesViewDecereal games,
        BigDecimal soloPrice,
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
                tournament.getStatus() != null ? tournament.getStatus().getValue() : null,
                tournament.getGames() != null ? GamesViewDecereal.fromGamesViewClient(tournament.getGames()) : null,
                tournament.getSoloPrice(),
                tournament.getCurrency(),
                tournament.getStartsAt(),
                tournament.getEndsAt(),
                tournament.getCreatedAt()
        );
    }
}
