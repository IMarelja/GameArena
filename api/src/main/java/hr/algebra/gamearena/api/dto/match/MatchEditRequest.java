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
public class MatchEditRequest {
    @NotNull(message = "Player one is required")
    private Long playerOneId;

    @NotNull(message = "Player two is required")
    private Long playerTwoId;

    private Integer playerOneScore;

    private Integer playerTwoScore;

    private Long winnerId;

    @NotNull(message = "Status is required")
    private MatchStatusView status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime scheduledAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime playedAt;
}
