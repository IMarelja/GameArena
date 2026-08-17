package hr.algebra.gamearena.api.orm.postgres;

import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberSave;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberUpdate;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Entity
@Table(name = "tournament_member")
@DynamicInsert
public class TournamentMemberPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private TournamentMemberRole role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    public TournamentMemberPostgres fromTournamentMemberSave(TournamentMemberSave save) {
        this.tournamentId = save.getTournamentId();
        this.userId = save.getUserId();
        this.role = save.getRole();
        this.joinedAt = LocalDateTime.now(ZoneId.of("UTC"));
        return this;
    }

    public TournamentMemberPostgres fromTournamentMemberUpdate(TournamentMemberUpdate update) {
        this.role = update.getRole();
        return this;
    }
}
