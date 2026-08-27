package hr.algebra.gamearena.api.dto.tournament.member;

import hr.algebra.gamearena.api.dto.user.UserJustUsernameView;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMember;
import hr.algebra.gamearena.api.model.user.User;

import java.util.Optional;

public record TournamentMemberView(
        Long id,
        UserJustUsernameView user,
        TournamentMemberRoleView role
) {
    public static TournamentMemberView fromTournamentMember(TournamentMember tournamentMember, Optional<User> user) {
        return new TournamentMemberView(
                tournamentMember.id(),
                user.map(UserJustUsernameView::fromUser)
                        .orElse(UserJustUsernameView.deleteUser()),
                TournamentMemberRoleView.fromTournamentMemberRole(tournamentMember.role())
        );
    }
}
