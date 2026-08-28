package hr.algebra.gamearena.webapp.models.cereal.team.member;

import com.gamearena.client.model.TeamMemberFullView;
import com.gamearena.client.model.TeamMemberMinimalView;

public enum TeamMemberRoleDecereal {
    CAPTAIN,
    REGULAR,
    ERROR;

    public static TeamMemberRoleDecereal fromTeamMemberRoleClient(TeamMemberMinimalView.RoleEnum role) {
        return switch (role) {
            case CAPTAIN -> CAPTAIN;
            case REGULAR -> REGULAR;
            default -> ERROR;
        };
    }

    public static TeamMemberRoleDecereal fromTeamMemberFullViewRoleClient(TeamMemberFullView.RoleEnum role) {
        return switch (role) {
            case CAPTAIN -> CAPTAIN;
            case REGULAR -> REGULAR;
            default -> ERROR;
        };
    }
}
