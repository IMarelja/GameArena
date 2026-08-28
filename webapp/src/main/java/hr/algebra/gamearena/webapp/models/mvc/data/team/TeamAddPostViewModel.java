package hr.algebra.gamearena.webapp.models.mvc.data.team;

import hr.algebra.gamearena.webapp.models.cereal.team.TeamAddCereal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamAddPostViewModel(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "A game must be selected")
        Long gameId
) {
    public TeamAddCereal toTeamAddCereal() {
        return new TeamAddCereal(name, gameId);
    }
}
