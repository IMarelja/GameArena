package hr.algebra.gamearena.api.dto.team;

import hr.algebra.gamearena.api.dto.games.GamesView;
import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.model.team.Team;

import java.time.LocalDateTime;
import java.util.Optional;

public record TeamMinimalView(
        Long id,
        String name,
        GamesView game,
        LocalDateTime createdAt,
        Long memberCount
) {
    public static TeamMinimalView fromTeamAndGame(Team team, Optional<Games> game, Long memberCount) {
        return new TeamMinimalView(
                team.id(),
                team.name(),
                game.map(GamesView::fromGamesModel)
                        .orElse(GamesView.deletedGame()),
                team.created_at(),
                memberCount
        );
    }
}
