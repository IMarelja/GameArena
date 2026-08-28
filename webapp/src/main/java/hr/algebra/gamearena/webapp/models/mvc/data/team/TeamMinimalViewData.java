package hr.algebra.gamearena.webapp.models.mvc.data.team;

import hr.algebra.gamearena.webapp.models.cereal.team.TeamMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewsData;

import java.time.OffsetDateTime;

public record TeamMinimalViewData(
        Long id,
        String name,
        GameViewsData game,
        OffsetDateTime createdAt,
        Long memberCount
) {
    public static TeamMinimalViewData from(TeamMinimalViewDecereal team) {
        return new TeamMinimalViewData(
                team.id(),
                team.name(),
                GameViewsData.fromGamesViewDecereal(team.game()),
                team.createdAt(),
                team.memberCount()
        );
    }
}
