package hr.algebra.gamearena.api.model.tournament.member;

import hr.algebra.gamearena.api.orm.postgres.tournament.TournamentMemberPostgres;

import java.time.LocalDateTime;

public record TournamentMember(
        Long id,
        Long tournamentId,
        Long userId,
        TournamentMemberRole role,
        LocalDateTime joinedAt
) {
    public static TournamentMember fromTournamentMemberPostgres(TournamentMemberPostgres tournamentMemberPostgres) {
        return new TournamentMember(
                tournamentMemberPostgres.getId(),
                tournamentMemberPostgres.getTournamentId(),
                tournamentMemberPostgres.getUserId(),
                tournamentMemberPostgres.getRole(),
                tournamentMemberPostgres.getJoinedAt().toLocalDateTime()
        );
    }
}
