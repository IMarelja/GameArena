package hr.algebra.gamearena.api.orm.postgres.tournament;

import hr.algebra.gamearena.api.model.tournament.TournamentSave;
import hr.algebra.gamearena.api.model.tournament.TournamentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Entity
@Table(name = "tournaments")
@DynamicInsert
public class TournamentPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TournamentStatus status;

    @Column(name = "price_solo", nullable = false, precision = 12, scale = 2)
    private BigDecimal priceSolo;

    @Column(name = "price_group", nullable = false, precision = 12, scale = 2)
    private BigDecimal priceGroup;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "starts_at", nullable = false)
    private OffsetDateTime startsAt;

    @Column(name = "ends_at")
    private OffsetDateTime endsAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public TournamentPostgres fromTournamentSave(TournamentSave save) {
        this.name = save.getName();
        this.description = StringUtils.hasText(save.getDescription())
                ? save.getDescription()
                : null;
        this.gameId = save.getGameId();
        this.status = save.getStatus();

        this.priceSolo = save.getPriceSolo();
        this.priceGroup = save.getPriceGroup();
        this.currency = save.getCurrency();

        this.startsAt = save.getStartsAt();
        this.endsAt = save.getEndsAt() != null ? save.getEndsAt() : null;
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        return this;
    }
}
