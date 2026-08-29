package hr.algebra.gamearena.webapp.models.mvc.data.admin.games;

import hr.algebra.gamearena.webapp.models.cereal.games.GameCreateCereal;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record GameCreatePostViewModel(
        @NotBlank(message = "Name is required")
        String name,

        @Nullable
        String description
) {
        public GameCreateCereal getCereal() {
                return new GameCreateCereal(
                        name,
                        description
                );
        }
}
