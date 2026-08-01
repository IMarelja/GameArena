package hr.algebra.gamearena.api.dto.games;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GamesUpdateRequest {
    @NotBlank
    @NotNull
    private String name;

    private String description;

    @NotNull
    private Boolean isActive;
}
