package hr.algebra.gamearena.api.orm.postgres;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

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
}
