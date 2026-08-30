package hr.algebra.gamearena.webapp.models.mvc.data.team.invite;

import hr.algebra.gamearena.webapp.models.cereal.team.invitation.InviteStatusDecereal;

public enum InviteStatusDataEnum {
    PENDING,
    ACCEPTED,
    DECLINED,
    CANCELLED,
    ERROR;

    public static InviteStatusDataEnum fromInvite(InviteStatusDecereal invite) {
        return switch (invite){
            case PENDING -> PENDING;
            case ACCEPTED -> ACCEPTED;
            case DECLINED -> DECLINED;
            case CANCELLED -> CANCELLED;
            case ERROR -> ERROR;
        };
    }
}
