package hr.algebra.gamearena.api.dto.tournament;

import hr.algebra.gamearena.api.dto.games.GamesView;
import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.model.tournament.Tournament;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TournamentFullView(
    Long id,
    String name,
    String description,
    TournamentStatusView status,
    GamesView games,
    BigDecimal soloPrice,
    String currency,
    LocalDateTime startsAt,
    LocalDateTime endsAt,
    LocalDateTime createdAt

) {
    public static TournamentFullView fromTournamentAndGame(Tournament tournament, Games games) {
        return new TournamentFullView(
                tournament.id(),
                tournament.name(),
                tournament.description(),
                TournamentStatusView.fromTournamentStatus(tournament.status()),
                GamesView.fromGamesModel(games),
                tournament.soloPrice(),
                tournament.currency(),
                tournament.startsAt(),
                tournament.endsAt(),
                tournament.createdAt()
        );
    }
}
