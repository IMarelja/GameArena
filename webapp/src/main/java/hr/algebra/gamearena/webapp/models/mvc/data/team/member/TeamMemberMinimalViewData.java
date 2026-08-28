package hr.algebra.gamearena.webapp.models.mvc.data.team.member;

import hr.algebra.gamearena.webapp.models.cereal.team.member.TeamMemberMinimalViewDecereal;

public record TeamMemberMinimalViewData(
        Long memberId,
        Long userId,
        String username,
        TeamMemberRoleViewEnum role
) {
    public static TeamMemberMinimalViewData from(TeamMemberMinimalViewDecereal member) {
        return new TeamMemberMinimalViewData(
                member.memberId(),
                member.userId(),
                member.username(),
                TeamMemberRoleViewEnum.fromDecereal(member.role())
        );
    }
}
