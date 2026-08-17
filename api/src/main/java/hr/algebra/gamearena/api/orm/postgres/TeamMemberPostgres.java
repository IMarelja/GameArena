package hr.algebra.gamearena.api.orm.postgres;

import hr.algebra.gamearena.api.model.team.TeamMemberRole;
import hr.algebra.gamearena.api.model.team.TeamMemberSave;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Entity
@Table(name = "team_members")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private TeamMemberRole role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    public TeamMemberPostgres fromTeamMemberSave(TeamMemberSave save) {
        this.teamId = save.getTeamId();
        this.userId = save.getUserId();
        this.role = save.getRole();
        this.joinedAt = LocalDateTime.now(ZoneId.of("UTC"));
        return this;
    }
}
