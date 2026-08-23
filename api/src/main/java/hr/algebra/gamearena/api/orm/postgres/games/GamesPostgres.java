package hr.algebra.gamearena.api.orm.postgres.games;

import hr.algebra.gamearena.api.model.games.GamesSave;
import hr.algebra.gamearena.api.model.games.GamesUpdate;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
    private OffsetDateTime createdAt;

    public GamesPostgres fromGamesSave(
            GamesSave gamesSave
    ){
        this.name = gamesSave.getName();
        this.description = StringUtils.hasText(gamesSave.getDescription())
                ? gamesSave.getDescription()
                : null;
        this.isActive = true;
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        return this;
    }

    public GamesPostgres fromGamesUpdate(
            GamesUpdate gamesUpdate
    ){
        this.name = gamesUpdate.getName();
        this.description = StringUtils.hasText(gamesUpdate.getDescription())
                ? gamesUpdate.getDescription()
                : null;
        this.isActive = gamesUpdate.isActive();
        return this;
    }
}
