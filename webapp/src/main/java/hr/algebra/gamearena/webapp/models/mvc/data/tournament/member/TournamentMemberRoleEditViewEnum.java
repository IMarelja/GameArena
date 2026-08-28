package hr.algebra.gamearena.webapp.models.mvc.data.tournament.member;

import com.gamearena.client.model.TournamentMemberEditRequest;
import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberRoleDecereal;

public enum TournamentMemberRoleEditViewEnum {
    ORGANIZER,
    PARTICIPANTS,
    DISQUALIFIED;

    public static TournamentMemberRoleEditViewEnum fromDecereal(TournamentMemberRoleDecereal role) {
        return switch (role) {
            case ORGANIZER -> ORGANIZER;
            case PARTICIPANTS -> PARTICIPANTS;
            case DISQUALIFIED -> DISQUALIFIED;
        };
    }

    public TournamentMemberEditRequest.RoleEnum toClient() {
        return switch (this) {
            case ORGANIZER -> TournamentMemberEditRequest.RoleEnum.ORGANIZER;
            case PARTICIPANTS -> TournamentMemberEditRequest.RoleEnum.PARTICIPANTS;
            case DISQUALIFIED -> TournamentMemberEditRequest.RoleEnum.DISQUALIFIED;
        };
    }
}
