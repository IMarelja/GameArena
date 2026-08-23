package hr.algebra.gamearena.api.dto.team.invitation;

import hr.algebra.gamearena.api.model.team.InviteStatus;

public enum InviteStatusView {
    PENDING,
    ACCEPTED,
    DECLINED,
    CANCELLED;

    public static InviteStatusView fromInviteStatus(InviteStatus status) {
        return switch (status) {
            case PENDING -> PENDING;
            case ACCEPTED -> ACCEPTED;
            case DECLINED -> DECLINED;
            case CANCELLED -> CANCELLED;
        };
    }
}
