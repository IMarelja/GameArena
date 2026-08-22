package hr.algebra.gamearena.api.orm.postgres.tournament;

import hr.algebra.gamearena.api.model.tournament.group.TournamentGroupSave;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Entity
@Table(name = "tournament_groups")
@DynamicInsert
public class TournamentGroupPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public TournamentGroupPostgres fromTournamentGroupSave(TournamentGroupSave save) {
        this.tournamentId = save.getTournamentId();
        this.name = save.getName();
        this.createdBy = save.getCreatedBy();
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        return this;
    }
}
