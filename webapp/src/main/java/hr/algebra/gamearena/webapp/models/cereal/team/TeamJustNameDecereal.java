package hr.algebra.gamearena.webapp.models.cereal.team;

import com.gamearena.client.model.TeamJustNameDto;

public record TeamJustNameDecereal(
        Long id,
        String name
) {
    private static final TeamJustNameDecereal UNAVAILABLE = new TeamJustNameDecereal(-1L, "Unknown Team");

    public static TeamJustNameDecereal fromTeamJustNameClient(TeamJustNameDto team) {
        return new TeamJustNameDecereal(
                team.getId(),
                team.getName()
        );
    }

    public static TeamJustNameDecereal fromTeamJustNameClientOrUnavailableGarbage(TeamJustNameDto team) {
        return team != null ? fromTeamJustNameClient(team) : UNAVAILABLE;
    }
}
