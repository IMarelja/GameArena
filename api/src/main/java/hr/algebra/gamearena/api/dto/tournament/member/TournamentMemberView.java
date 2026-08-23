package hr.algebra.gamearena.api.dto.tournament.member;

import hr.algebra.gamearena.api.dto.user.UserJustUsernameView;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMember;
import hr.algebra.gamearena.api.model.user.User;

public record TournamentMemberView(
        Long id,
        UserJustUsernameView user,
        TournamentMemberRoleView role
) {
    public static TournamentMemberView fromTournamentMember(TournamentMember tournamentMember, User user) {
        return new TournamentMemberView(
                tournamentMember.id(),
                user != null ? UserJustUsernameView.fromUser(user) : null,
                TournamentMemberRoleView.fromTournamentMemberRole(tournamentMember.role())
        );
    }
}
