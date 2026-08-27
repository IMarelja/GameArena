package hr.algebra.gamearena.webapp.models.cereal.team;

import com.gamearena.client.model.TeamMinimalView;
import hr.algebra.gamearena.webapp.models.cereal.games.GamesViewDecereal;

import java.time.OffsetDateTime;

public record TeamMinimalViewDecereal(
        Long id,
        String name,
        GamesViewDecereal game,
        OffsetDateTime createdAt,
        Long memberCount
) {
    public static TeamMinimalViewDecereal fromTeamMinimalViewClient(TeamMinimalView team) {
        return new TeamMinimalViewDecereal(
                team.getId(),
                team.getName(),
                GamesViewDecereal.fromGamesViewClientOrUnavailableGarbage(team.getGame()),
                team.getCreatedAt(),
                team.getMemberCount()
        );
    }
}
