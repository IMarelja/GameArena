package hr.algebra.gamearena.webapp.models.mvc.data.team;

import hr.algebra.gamearena.webapp.models.cereal.team.TeamJustNameDecereal;

public record TeamJustNameViewData(
        Long id,
        String name
) {
    public static TeamJustNameViewData fromTeamJustNameDecereal(TeamJustNameDecereal decereal) {
        return new TeamJustNameViewData(
                decereal.id(),
                decereal.name()
        );
    }
}
