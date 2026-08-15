package hr.algebra.gamearena.api.model.team;


import hr.algebra.gamearena.api.dto.team.invitation.InviteResponseStatus;
import hr.algebra.gamearena.api.dto.team.invitation.InviterInviteStatus;

public enum InviteStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
    CANCELLED;

    public InviteStatus fromInviteResponseStatus(InviteResponseStatus status) {
        return switch (status) {
            case ACCEPT -> ACCEPTED;
            case DECLINE -> DECLINED;
        };
    }

    public InviteStatus fromInvitedInviteStatus(InviterInviteStatus status) {
        return switch (status) {
            case CANCEL -> CANCELLED;
        };
    }
}
