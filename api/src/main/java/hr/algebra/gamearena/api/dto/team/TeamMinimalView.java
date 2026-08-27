package hr.algebra.gamearena.api.dto.team;

import hr.algebra.gamearena.api.dto.games.GamesView;
import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.model.team.Team;

import java.time.LocalDateTime;

public record TeamMinimalView(
        Long id,
        String name,
        GamesView game,
        LocalDateTime createdAt,
        Long memberCount
) {
    public static TeamMinimalView fromTeamAndGame(Team team, Games game, Long memberCount) {
        return new TeamMinimalView(
                team.id(),
                team.name(),
                GamesView.fromGamesModelOrNotFound(game),
                team.created_at(),
                memberCount
        );
    }
}
