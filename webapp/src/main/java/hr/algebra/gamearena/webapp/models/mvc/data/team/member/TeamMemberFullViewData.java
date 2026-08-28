package hr.algebra.gamearena.webapp.models.mvc.data.team.member;

import hr.algebra.gamearena.webapp.models.cereal.team.member.TeamMemberFullViewDecereal;
import hr.algebra.gamearena.webapp.models.mvc.data.user.UserViewData;

import java.time.OffsetDateTime;

public record TeamMemberFullViewData(
        Long memberId,
        Long teamId,
        TeamMemberRoleViewEnum role,
        UserViewData user,
        OffsetDateTime joinedAt
) {
    public static TeamMemberFullViewData from(TeamMemberFullViewDecereal member) {
        return new TeamMemberFullViewData(
                member.memberId(),
                member.teamId(),
                TeamMemberRoleViewEnum.fromDecereal(member.role()),
                UserViewData.from(member.user()),
                member.joinedAt()
        );
    }
}
