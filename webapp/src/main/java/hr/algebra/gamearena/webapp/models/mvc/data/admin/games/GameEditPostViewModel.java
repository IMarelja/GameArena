package hr.algebra.gamearena.webapp.models.mvc.data.admin.games;

import hr.algebra.gamearena.webapp.models.cereal.games.GameEditCereal;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record GameEditPostViewModel(
        @NotBlank(message = "Name is required")
        String name,

        @Nullable
        String description,

        @NotNull(message = "Must set the status of the ")
        Boolean isActive
) {
        public GameEditCereal getCereal() {
                return new GameEditCereal(
                        name,
                        description,
                        isActive
                );
        }
}
