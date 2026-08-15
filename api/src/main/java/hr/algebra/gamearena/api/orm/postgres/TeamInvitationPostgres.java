package hr.algebra.gamearena.api.orm.postgres;

import hr.algebra.gamearena.api.model.team.InviteStatus;
import hr.algebra.gamearena.api.model.team.TeamInvitationSave;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
    private InviteStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    public TeamInvitationPostgres fromTeamInvitationSave(TeamInvitationSave save) {
        this.teamId = save.getTeamId();
        this.inviteeId = save.getInviteeId();
        this.inviterId = save.getInviterId();
        this.status = save.getStatus();
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC"));
        return this;
    }
}
