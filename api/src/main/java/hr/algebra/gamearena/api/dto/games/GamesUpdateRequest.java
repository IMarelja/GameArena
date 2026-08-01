package hr.algebra.gamearena.api.dto.games;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class GamesUpdateRequest {
    @NotBlank
    @NotNull
    private String name;

    @Null
    private Optional<String> description;

    @NotNull
    private boolean isActive;
}
