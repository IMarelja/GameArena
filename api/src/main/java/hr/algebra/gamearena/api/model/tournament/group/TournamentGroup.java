package hr.algebra.gamearena.api.model.tournament.group;

import hr.algebra.gamearena.api.orm.postgres.tournament.TournamentGroupPostgres;

import java.time.LocalDateTime;

public record TournamentGroup(
        Long id,
        Long tournamentId,
        String name,
        Long createdBy,
        LocalDateTime createdAt
) {
    public static TournamentGroup fromTournamentGroupPostgres(TournamentGroupPostgres tournamentGroupPostgres) {
        return new TournamentGroup(
                tournamentGroupPostgres.getId(),
                tournamentGroupPostgres.getTournamentId(),
                tournamentGroupPostgres.getName(),
                tournamentGroupPostgres.getCreatedBy(),
                tournamentGroupPostgres.getCreatedAt().toLocalDateTime()
        );
    }
}
