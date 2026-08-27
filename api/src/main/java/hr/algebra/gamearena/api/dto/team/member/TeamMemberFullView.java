package hr.algebra.gamearena.api.dto.team.member;

import hr.algebra.gamearena.api.dto.user.UserViewDto;
import hr.algebra.gamearena.api.model.team.TeamMember;
import hr.algebra.gamearena.api.model.user.User;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public record TeamMemberFullView(
        Long memberId,
        Long teamId,
        TeamMemberRoleView role,
        UserViewDto user,
        OffsetDateTime joinedAt
) {
    public static TeamMemberFullView fromTeamMemberAndUser(TeamMember member, Optional<User> user) {
        return new TeamMemberFullView(
                member.id(),
                member.teamId(),
                TeamMemberRoleView.fromTeamMemberRole(member.role()),
                user.map(UserViewDto::fromUser)
                        .orElseGet(UserViewDto::deletedUser),
                member.joinedAt().atOffset(ZoneOffset.UTC)
        );
    }
}
