package hr.algebra.gamearena.api.model.games;

import hr.algebra.gamearena.api.orm.postgres.GamesPostgres;

import java.time.LocalDateTime;
import java.util.Optional;

public record Games (
        Long id,
        String name,
        Optional<String> description,
        boolean isActive,
        LocalDateTime createAt
){
    public static Games fromPostgres(GamesPostgres gamesPostgres) {
        return new Games(
                gamesPostgres.getId(),
                gamesPostgres.getName(),
                Optional.ofNullable(gamesPostgres.getDescription()),
                gamesPostgres.getIsActive(),
                gamesPostgres.getCreatedAt().toLocalDateTime()
        );
    }
}
