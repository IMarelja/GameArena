package hr.algebra.gamearena.api.orm.postgres.team;

import hr.algebra.gamearena.api.model.team.TeamMemberSave;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Entity
@Table(name = "team_members", indexes = @Index(name = "idx_team_members_user_id", columnList = "user_id"))
@DynamicInsert
public class TeamMemberPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id")
    private Long teamMemberId;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @Column(name = "joined_at", nullable = false)
    private OffsetDateTime joinedAt;

    public TeamMemberPostgres fromTeamMemberSave(TeamMemberSave save) {
        this.teamId = save.getTeamId();
        this.userId = save.getUserId();
        this.role = save.getRole().toString();
        this.joinedAt = OffsetDateTime.now(ZoneOffset.UTC);
        return this;
    }
}
