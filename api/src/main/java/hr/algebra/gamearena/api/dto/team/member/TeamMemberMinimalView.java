package hr.algebra.gamearena.api.dto.team.member;

import hr.algebra.gamearena.api.model.team.TeamMember;
import hr.algebra.gamearena.api.model.user.User;

public record TeamMemberMinimalView(
        Long memberId,
        Long userId,
        String username,
        TeamMemberRoleView role
) {
    public static TeamMemberMinimalView fromTeamMemberAndUser(TeamMember teamMember, User user) {
        return new TeamMemberMinimalView(
                teamMember.id(),
                teamMember.userId(),
                user.username(),
                TeamMemberRoleView.fromTeamMemberRole(teamMember.role())
        );
    }
}
