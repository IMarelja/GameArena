package hr.algebra.gamearena.api.dto.games;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GamesQueryDto {
    @NotNull(message = "Sorting order by creation is required")
    Boolean ascending;
}
