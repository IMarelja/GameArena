package hr.algebra.gamearena.api.dto.tournament.member;

import hr.algebra.gamearena.api.model.tournament.member.TournamentMember;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;

public record TournamentMemberView(
        Long id,
        Long userId,
        TournamentMemberRole role
) {
    public static TournamentMemberView fromTournamentMember(TournamentMember tournamentMember) {
        return new TournamentMemberView(
                tournamentMember.id(),
                tournamentMember.userId(),
                tournamentMember.role()
        );
    }
}
