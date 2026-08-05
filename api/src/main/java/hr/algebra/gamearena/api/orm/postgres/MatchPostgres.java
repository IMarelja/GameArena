package hr.algebra.gamearena.api.orm.postgres;

import hr.algebra.gamearena.api.model.match.MatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "matches")
@DynamicInsert
public class MatchPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tournament_id")
    private Long tournamentId;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "player_one_id", nullable = false)
    private Long playerOneId;

    @Column(name = "player_two_id", nullable = false)
    private Long playerTwoId;

    @Column(name = "player_one_score")
    private Integer playerOneScore;

    @Column(name = "player_two_score")
    private Integer playerTwoScore;

    @Column(name = "winner_id")
    private Long winnerId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "match_status")
    private MatchStatus status;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "played_at")
    private LocalDateTime playedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
