package hr.algebra.gamearena.webapp.models.cereal.team.invitation;

import com.gamearena.client.model.TeamInvitationView;

public enum InviteStatusDecereal {
    PENDING,
    ACCEPTED,
    DECLINED,
    CANCELLED,
    ERROR;

    public static InviteStatusDecereal fromTeamInvitationViewStatusClient(TeamInvitationView.StatusEnum status) {
        if(status == null) {
            return InviteStatusDecereal.ERROR;
        }

        return switch (status) {
            case PENDING -> PENDING;
            case ACCEPTED -> ACCEPTED;
            case DECLINED -> DECLINED;
            case CANCELLED -> CANCELLED;
            default -> ERROR;
        };
    }
}
