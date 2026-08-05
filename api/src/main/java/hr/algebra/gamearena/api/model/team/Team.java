package hr.algebra.gamearena.api.model.team;

import hr.algebra.gamearena.api.orm.postgres.TeamPostgres;

import java.time.LocalDateTime;

public record Team(
        Long id,
        String name,
        Long game_id,
        Long captain_id,
        LocalDateTime created_at
) {
    public static Team fromPostgresTeam(TeamPostgres teamPostgres) {
        return new Team(
                teamPostgres.getId(),
                teamPostgres.getName(),
                teamPostgres.getGameId(),
                teamPostgres.getCaptainId(),
                teamPostgres.getCreatedAt()
        );
    }
}
