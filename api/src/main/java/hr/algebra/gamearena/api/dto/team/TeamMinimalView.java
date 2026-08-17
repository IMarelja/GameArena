package hr.algebra.gamearena.api.dto.team;

import hr.algebra.gamearena.api.model.team.Team;

import java.time.LocalDateTime;

public record TeamMinimalView(
        Long id,
        String name,
        Long gameId,
        LocalDateTime createdAt,
        Long memberCount
) {
    public static TeamMinimalView fromTeam(Team team, Long memberCount) {
        return new TeamMinimalView(
                team.id(),
                team.name(),
                team.game_id(),
                team.created_at(),
                memberCount
        );
    }
}
