package hr.algebra.gamearena.webapp.models.mvc.data.team;

import hr.algebra.gamearena.webapp.models.cereal.team.TeamEditCereal;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMinimalViewDecereal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamEditPostViewModel(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "A game must be selected")
        Long gameId
) {
    public static TeamEditPostViewModel fromTeamMinimalViewDecereal(TeamMinimalViewDecereal team) {
        return new TeamEditPostViewModel(team.name(), team.game() != null ? team.game().id() : null);
    }

    public TeamEditCereal toTeamEditCereal() {
        return new TeamEditCereal(name, gameId);
    }
}
