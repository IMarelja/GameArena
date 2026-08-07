package hr.algebra.gamearena.api.orm.postgres;

import hr.algebra.gamearena.api.model.team.TeamSave;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Entity
@Table(name = "teams")
@DynamicInsert
public class TeamPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "captain_id", nullable = false)
    private Long captainId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public TeamPostgres fromTeamSave(TeamSave teamSave) {
        this.name = teamSave.getName();
        this.captainId = teamSave.getCaptainId();
        this.gameId = teamSave.getGameId();
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC"));
        return this;
    }
}
