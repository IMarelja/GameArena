package hr.algebra.gamearena.api.orm.postgres.tournament;

import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberSave;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberUpdate;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
    private OffsetDateTime joinedAt;

    @Column(name = "payer_id")
    private Long payerId;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "confirmed", nullable = false)
    private Boolean confirmed;

    public TournamentMemberPostgres fromTournamentMemberSave(TournamentMemberSave save) {
        this.tournamentId = save.getTournamentId();
        this.userId = save.getUserId();
        this.role = save.getRole();
        this.joinedAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.payerId = (save.getPayerId() != null)
                ? save.getPayerId()
                : null;
        this.groupId = (save.getGroupId() != null)
                ? save.getGroupId()
                : null;
        this.paymentId = (save.getPaymentId() != null)
                ? save.getPaymentId()
                : null;
        this.confirmed = false;
        return this;
    }

    public TournamentMemberPostgres fromTournamentMemberUpdate(TournamentMemberUpdate update) {
        this.role = update.getRole();
        this.groupId = update.getGroupId();
        return this;
    }
}
