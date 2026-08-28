package hr.algebra.gamearena.webapp.models.mvc.data.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamAddPutViewModel(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "A game must be selected")
        Long gameId
) {
}
