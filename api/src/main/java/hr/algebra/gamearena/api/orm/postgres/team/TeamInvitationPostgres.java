package hr.algebra.gamearena.api.orm.postgres.team;

import hr.algebra.gamearena.api.model.team.TeamInvitationSave;
import hr.algebra.gamearena.api.model.team.TeamInvitationUpdate;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Entity
@Table(name = "team_invitations")
@DynamicInsert
public class TeamInvitationPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long invitationId;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "inviter_id", nullable = false)
    private Long inviterId;

    @Column(name = "invitee_id", nullable = false)
    private Long inviteeId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "invite_status")
    private invite_status status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "responded_at")
    private OffsetDateTime respondedAt;

    public TeamInvitationPostgres fromTeamInvitationSave(TeamInvitationSave save) {
        this.teamId = save.getTeamId();
        this.inviteeId = save.getInviteeId();
        this.inviterId = save.getInviterId();
        this.status = invite_status.fromInviteStatus(save.getStatus());
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.respondedAt = null;
        return this;
    }

    public TeamInvitationPostgres fromTeamInvitationUpdate(TeamInvitationUpdate update) {
        this.status = invite_status.fromInviteStatus(update.getStatus());
        this.respondedAt = OffsetDateTime.now(ZoneOffset.UTC);
        return this;
    }
}
