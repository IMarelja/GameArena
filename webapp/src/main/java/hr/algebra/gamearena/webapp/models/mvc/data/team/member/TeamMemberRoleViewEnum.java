package hr.algebra.gamearena.webapp.models.mvc.data.team.member;

import com.gamearena.client.model.TeamMemberEditRequest;
import hr.algebra.gamearena.webapp.models.cereal.team.member.TeamMemberRoleDecereal;

public enum TeamMemberRoleViewEnum {
    CAPTAIN,
    REGULAR;

    public static TeamMemberRoleViewEnum fromDecereal(TeamMemberRoleDecereal role) {
        return switch (role) {
            case CAPTAIN -> CAPTAIN;
            case REGULAR, ERROR -> REGULAR;
        };
    }

    public TeamMemberEditRequest.RoleEnum toClient() {
        return switch (this) {
            case CAPTAIN -> TeamMemberEditRequest.RoleEnum.CAPTAIN;
            case REGULAR -> TeamMemberEditRequest.RoleEnum.REGULAR;
        };
    }

    public String badgeClass() {
        return switch (this) {
            case CAPTAIN -> "text-bg-primary";
            case REGULAR -> "text-bg-secondary";
        };
    }
}
