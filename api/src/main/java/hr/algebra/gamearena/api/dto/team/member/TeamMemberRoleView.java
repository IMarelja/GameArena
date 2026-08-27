package hr.algebra.gamearena.api.dto.team.member;

import hr.algebra.gamearena.api.model.team.TeamMemberRole;

public enum TeamMemberRoleView {
    CAPTAIN,
    REGULAR;

    public static TeamMemberRoleView fromTeamMemberRole(TeamMemberRole role) {
        return switch (role) {
            case CAPTAIN -> CAPTAIN;
            case REGULAR -> REGULAR;
        };
    }

    public TeamMemberRole toTeamMemberRole() {
        return switch (this) {
            case CAPTAIN -> TeamMemberRole.CAPTAIN;
            case REGULAR -> TeamMemberRole.REGULAR;
        };
    }
}
