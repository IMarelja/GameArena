package hr.algebra.gamearena.webapp.models.mvc.data.team.member;

import hr.algebra.gamearena.webapp.models.cereal.team.member.TeamMemberEditCereal;
import jakarta.validation.constraints.NotNull;

public record TeamMemberEditPostViewModel(
        @NotNull(message = "Role is required")
        TeamMemberRoleViewEnum role
) {
    public TeamMemberEditCereal toTeamMemberEditCereal() {
        return new TeamMemberEditCereal(role);
    }
}
