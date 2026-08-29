package hr.algebra.gamearena.webapp.models.mvc.data.team;

import hr.algebra.gamearena.webapp.models.cereal.team.TeamMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewData;

import java.time.OffsetDateTime;

public record TeamMinimalViewData(
        Long id,
        String name,
        GameViewData game,
        OffsetDateTime createdAt,
        Long memberCount
) {
    public static TeamMinimalViewData from(TeamMinimalViewDecereal team) {
        return new TeamMinimalViewData(
                team.id(),
                team.name(),
                GameViewData.fromGamesViewDecereal(team.game()),
                team.createdAt(),
                team.memberCount()
        );
    }
}
