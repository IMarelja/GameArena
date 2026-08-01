package hr.algebra.gamearena.api.dto.games;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GamesCreateRequest {
    @NotBlank
    private String name;

    private String description;
}
