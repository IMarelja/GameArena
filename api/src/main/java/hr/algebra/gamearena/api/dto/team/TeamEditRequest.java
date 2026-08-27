package hr.algebra.gamearena.api.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamEditRequest {
    @NotBlank(message = "Name of your team is required")
    private String name;

    @NotNull(message = "Selecting a game to represent your team is required")
    private Long gameId;
}
