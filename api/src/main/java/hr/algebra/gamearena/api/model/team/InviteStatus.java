package hr.algebra.gamearena.api.model.team;


import hr.algebra.gamearena.api.dto.team.invitation.InviteResponseStatus;
import hr.algebra.gamearena.api.dto.team.invitation.InviterInviteStatus;
import hr.algebra.gamearena.api.orm.postgres.team.invite_status;

public enum InviteStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
    CANCELLED;

    public static InviteStatus fromInviteStatus(invite_status status) {
        return switch (status) {
            case PENDING -> PENDING;
            case ACCEPTED -> ACCEPTED;
            case DECLINED -> DECLINED;
            case CANCELLED -> CANCELLED;
        };
    }

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
