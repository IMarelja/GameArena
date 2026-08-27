package hr.algebra.gamearena.webapp.models.mvc.data.team;

import hr.algebra.gamearena.webapp.models.cereal.team.TeamMemberFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMemberMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMinimalViewDecereal;

import java.util.List;

public record TeamDetailViewData(
        TeamMinimalViewDecereal team,
        List<TeamMemberMinimalViewDecereal> members,
        TeamMemberFullViewDecereal meMember
) {
}
