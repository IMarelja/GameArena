package hr.algebra.gamearena.webapp.models.mvc.data.tournament.member;

import com.gamearena.client.model.TournamentMemberCreateRequest;

public enum TournamentMemberRoleCreateViewEnum {
    ORGANIZER,
    PARTICIPANTS;

    public TournamentMemberCreateRequest.RoleEnum toClient() {
        return switch (this) {
            case ORGANIZER -> TournamentMemberCreateRequest.RoleEnum.ORGANIZER;
            case PARTICIPANTS -> TournamentMemberCreateRequest.RoleEnum.PARTICIPANTS;
        };
    }
}
