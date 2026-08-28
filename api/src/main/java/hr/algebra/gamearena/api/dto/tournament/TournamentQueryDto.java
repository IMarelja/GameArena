package hr.algebra.gamearena.api.dto.tournament;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentQueryDto {
    Long gameId;

    @NotNull(message = "Sorting order by creation is required")
    Boolean ascending;
}
