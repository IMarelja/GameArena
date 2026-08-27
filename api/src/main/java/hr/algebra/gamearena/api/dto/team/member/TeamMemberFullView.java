package hr.algebra.gamearena.api.dto.team.member;

import hr.algebra.gamearena.api.dto.user.UserViewDto;
import hr.algebra.gamearena.api.model.team.TeamMember;
import hr.algebra.gamearena.api.model.user.User;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record TeamMemberFullView(
        Long memberId,
        Long teamId,
        TeamMemberRoleView role,
        UserViewDto user,
        OffsetDateTime joinedAt
) {
    public static TeamMemberFullView fromTeamMemberAndUser(TeamMember member, User user) {
        return new TeamMemberFullView(
                member.id(),
                member.teamId(),
                TeamMemberRoleView.fromTeamMemberRole(member.role()),
                UserViewDto.fromUser(user),
                member.joinedAt().atOffset(ZoneOffset.UTC)
        );
    }
}
