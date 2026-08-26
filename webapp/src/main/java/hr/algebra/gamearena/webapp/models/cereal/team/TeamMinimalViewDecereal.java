package hr.algebra.gamearena.webapp.models.cereal.team;

import com.gamearena.client.model.TeamMinimalView;

import java.time.OffsetDateTime;

public record TeamMinimalViewDecereal(
        Long id,
        String name,
        Long gameId,
        OffsetDateTime createdAt,
        Long memberCount
) {
    public static TeamMinimalViewDecereal fromTeamMinimalViewClient(TeamMinimalView team) {
        return new TeamMinimalViewDecereal(
                team.getId(),
                team.getName(),
                team.getGameId(),
                team.getCreatedAt(),
                team.getMemberCount()
        );
    }
}
