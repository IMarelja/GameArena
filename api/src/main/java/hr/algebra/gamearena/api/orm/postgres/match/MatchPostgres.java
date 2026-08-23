package hr.algebra.gamearena.api.orm.postgres.match;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.OffsetDateTime;

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

    @Column(name = "status", nullable = false, columnDefinition = "match_status")
    private String status;

    @Column(name = "scheduled_at")
    private OffsetDateTime scheduledAt;

    @Column(name = "played_at")
    private OffsetDateTime playedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
