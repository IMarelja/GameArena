package hr.algebra.gamearena.webapp.models.cereal.team.member;

import com.gamearena.client.model.TeamMemberMinimalView;

public record TeamMemberMinimalViewDecereal(
        Long memberId,
        Long userId,
        String username,
        TeamMemberRoleDecereal role
) {
    public static TeamMemberMinimalViewDecereal fromTeamMemberMinimalViewClient(TeamMemberMinimalView member) {
        return new TeamMemberMinimalViewDecereal(
                member.getMemberId(),
                member.getUserId(),
                member.getUsername(),
                TeamMemberRoleDecereal.fromTeamMemberRoleClient(member.getRole())
        );
    }
}
