package hr.algebra.gamearena.api.dto.match;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchCreateRequest {
    @NotNull(message = "Tournament must be selected to the match")
    private Long tournamentId;

    @NotNull(message = "Player one is required")
    private Long playerOneId;

    @NotNull(message = "Player two is required")
    private Long playerTwoId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime scheduledAt;
}
