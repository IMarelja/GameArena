package hr.algebra.gamearena.api.model.tournament;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class TournamentSave {
    private String name;
    private String description;
    private Long gameId;
    private TournamentStatus status;
    private BigDecimal priceSolo;
    private BigDecimal priceGroup;
    private String currency;
    private OffsetDateTime startsAt;
    private OffsetDateTime endsAt;
}
