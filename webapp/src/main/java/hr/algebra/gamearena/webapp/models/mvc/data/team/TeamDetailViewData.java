package hr.algebra.gamearena.webapp.models.mvc.data.team;

import hr.algebra.gamearena.webapp.models.mvc.data.team.member.TeamMemberFullViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.team.member.TeamMemberMinimalViewData;

import java.util.List;
import java.util.Optional;

public record TeamDetailViewData(
        TeamMinimalViewData team,
        Optional<List<TeamMemberMinimalViewData>> members,
        Optional<TeamMemberFullViewData> meMember
) {
}
