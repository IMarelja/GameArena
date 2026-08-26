package hr.algebra.gamearena.webapp.models.cereal.tournament;

import com.gamearena.client.model.TournamentMemberView;
import hr.algebra.gamearena.webapp.models.cereal.user.UserJustUsernameDecereal;

public record TournamentMemberViewDecereal(
        Long id,
        UserJustUsernameDecereal user,
        TournamentMemberRoleDecereal role
) {
    public static TournamentMemberViewDecereal fromTournamentMemberViewClient(TournamentMemberView member) {
        return new TournamentMemberViewDecereal(
                member.getId(),
                UserJustUsernameDecereal.fromUserJustUsernameClient(member.getUser()),
                TournamentMemberRoleDecereal.fromTournamentMemberRoleClient(member.getRole())
        );
    }
}
