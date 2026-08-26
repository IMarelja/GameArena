package hr.algebra.gamearena.webapp.models.cereal.team;

import com.gamearena.client.model.TeamMemberMinimalView;

public enum TeamMemberRoleDecereal {
    CAPTAIN,
    REGULAR;

    public static TeamMemberRoleDecereal fromTeamMemberRoleClient(TeamMemberMinimalView.RoleEnum role) {
        return switch (role) {
            case CAPTAIN -> CAPTAIN;
            case REGULAR -> REGULAR;
        };
    }

    public String badgeClass() {
        return switch (this) {
            case CAPTAIN -> "text-bg-primary";
            case REGULAR -> "text-bg-secondary";
        };
    }
}
