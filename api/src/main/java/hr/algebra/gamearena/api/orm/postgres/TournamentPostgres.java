package hr.algebra.gamearena.api.orm.postgres;

import hr.algebra.gamearena.api.model.tournament.TournamentSave;
import hr.algebra.gamearena.api.model.tournament.TournamentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

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

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at")
    private LocalDateTime endsAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public TournamentPostgres fromTournamentSave(TournamentSave save) {
        this.name = save.getName();
        this.description = StringUtils.hasText(save.getDescription())
                ? save.getDescription()
                : null;
        this.gameId = save.getGameId();
        this.status = save.getStatus();
        this.startsAt = save.getStartsAt();
        this.endsAt = save.getEndsAt() != null ? save.getEndsAt() : null;
        return this;
    }
}
