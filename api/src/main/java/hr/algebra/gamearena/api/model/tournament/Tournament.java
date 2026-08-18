package hr.algebra.gamearena.api.model.tournament;

import hr.algebra.gamearena.api.orm.postgres.TournamentPostgres;

import java.time.LocalDateTime;

public record Tournament(
        Long id,
        String name,
        String description,
        Long gameId,
        TournamentStatus status,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        LocalDateTime createdAt
) {
    public static Tournament fromTournamentPostgres(TournamentPostgres tournamentPostgres) {
        return new Tournament(
                tournamentPostgres.getId(),
                tournamentPostgres.getName(),
                tournamentPostgres.getDescription(),
                tournamentPostgres.getGameId(),
                tournamentPostgres.getStatus(),
                tournamentPostgres.getStartsAt().toLocalDateTime(),
                tournamentPostgres.getEndsAt() != null
                        ? tournamentPostgres.getEndsAt().toLocalDateTime()
                        : null,
                tournamentPostgres.getCreatedAt().toLocalDateTime()
        );
    }
}
