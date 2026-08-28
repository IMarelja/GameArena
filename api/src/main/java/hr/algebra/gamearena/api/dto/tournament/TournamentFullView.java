package hr.algebra.gamearena.api.dto.tournament;

import hr.algebra.gamearena.api.dto.games.GamesView;
import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.model.tournament.Tournament;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public record TournamentFullView(
    Long id,
    String name,
    String description,
    TournamentStatusView status,
    GamesView games,
    BigDecimal soloPrice,
    BigDecimal groupPrice,
    String currency,
    LocalDateTime startsAt,
    LocalDateTime endsAt,
    LocalDateTime createdAt

) {
    public static TournamentFullView fromTournamentAndGame(Tournament tournament, Optional<Games> games) {
        return new TournamentFullView(
                tournament.id(),
                tournament.name(),
                tournament.description(),
                TournamentStatusView.fromTournamentStatus(tournament.status()),
                games.map(GamesView::fromGamesModel)
                        .orElse(GamesView.deletedGame()),
                tournament.soloPrice(),
                tournament.groupPrice(),
                tournament.currency(),
                tournament.startsAt(),
                tournament.endsAt(),
                tournament.createdAt()
        );
    }
}
