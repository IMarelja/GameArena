package hr.algebra.gamearena.webapp.models.mvc.data.tournament.member;

import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberViewDecereal;
import hr.algebra.gamearena.webapp.models.mvc.data.user.UserJustUsernameViewData;

public record TournamentMemberViewData(
        Long id,
        UserJustUsernameViewData user,
        TournamentMemberRoleEnumData role
) {
    public static TournamentMemberViewData from(TournamentMemberViewDecereal member) {
        return new TournamentMemberViewData(
                member.id(),
                UserJustUsernameViewData.from(member.user()),
                TournamentMemberRoleEnumData.from(member.role())
        );
    }
}
