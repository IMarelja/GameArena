package hr.algebra.gamearena.api.dto.match;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchQueryDto{
        @Nullable
        Long gameId;

        @NotNull(message = "Sorting order by creation is required")
        Boolean ascending;
}