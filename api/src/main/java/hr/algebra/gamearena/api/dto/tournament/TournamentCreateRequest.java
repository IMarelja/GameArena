package hr.algebra.gamearena.api.dto.tournament;

import com.fasterxml.jackson.annotation.JsonFormat;
import hr.algebra.gamearena.api.dto.tournament.price.PriceCreateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentCreateRequest {
    @NotBlank(message = "Name of the tournament is required")
    private String name;

    private String description;

    @NotNull(message = "Selecting a game for the tournament is required")
    private Long gameId;

    @NotNull(message = "Price is required")
    @Valid
    private PriceCreateRequest price;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime startsAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime endsAt;
}
