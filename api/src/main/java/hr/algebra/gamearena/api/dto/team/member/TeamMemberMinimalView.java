package hr.algebra.gamearena.api.dto.team.member;

import hr.algebra.gamearena.api.model.team.Team;
import hr.algebra.gamearena.api.model.team.TeamMember;
import hr.algebra.gamearena.api.model.user.User;

public record TeamMemberMinimalView(
        Long memberId,
        Long userId,
        String username,
        Boolean isTeamCaptain
) {
    public static TeamMemberMinimalView fromTeamTeamMemberAndUser(Team team, TeamMember teamMember, User user) {
        boolean isCaptain = team.captain_id().equals(user.id());

        return new TeamMemberMinimalView(
                teamMember.id(),
                teamMember.userId(),
                user.username(),
                isCaptain
        );
    }
}
