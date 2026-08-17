package hr.algebra.gamearena.api.dto.tournament;

import hr.algebra.gamearena.api.dto.games.GamesView;
import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.model.tournament.Tournament;
import hr.algebra.gamearena.api.model.tournament.TournamentStatus;

import javax.swing.*;
import java.time.LocalDateTime;

public record TournamentFullView(
    Long id,
    String name,
    String description,
    TournamentStatus status,
    GamesView gamesView,
    LocalDateTime startsAt,
    LocalDateTime endsAt,
    LocalDateTime createdAt

) {
    public static TournamentFullView fromTournamentAndGame(Tournament tournament, Games games) {
        return new TournamentFullView(
                tournament.id(),
                tournament.name(),
                tournament.description(),
                tournament.status(),
                GamesView.fromGamesModel(games),
                tournament.startsAt(),
                tournament.endsAt(),
                tournament.createdAt()
        );
    }
}
