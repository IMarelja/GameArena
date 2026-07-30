package hr.algebra.gamearena.api.orm.postgres;

import hr.algebra.gamearena.api.model.games.GamesSave;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Getter
@Entity
@Table(name = "games")
@DynamicInsert
public class GamesPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public GamesPostgres fromGamesCreate(
            GamesSave gamesSave
    ){
        this.name = gamesSave.getName();
        this.description = Optional.ofNullable(gamesSave.getDescription())
                .flatMap(d -> d)
                .orElse(null);
        this.isActive = true;
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC"));
        return this;
    }
}
