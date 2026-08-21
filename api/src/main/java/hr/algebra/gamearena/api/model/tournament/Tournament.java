package hr.algebra.gamearena.api.model.tournament;

import hr.algebra.gamearena.api.orm.postgres.tournament.TournamentPostgres;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Tournament(
        Long id,
        String name,
        String description,
        Long gameId,
        BigDecimal soloPrice,
        BigDecimal groupPrice,
        String currency,
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

                tournamentPostgres.getPriceSolo(),
                tournamentPostgres.getPriceGroup(),
                tournamentPostgres.getCurrency(),

                tournamentPostgres.getStatus(),
                tournamentPostgres.getStartsAt().toLocalDateTime(),
                tournamentPostgres.getEndsAt() != null
                        ? tournamentPostgres.getEndsAt().toLocalDateTime()
                        : null,
                tournamentPostgres.getCreatedAt().toLocalDateTime()
        );
    }
}
