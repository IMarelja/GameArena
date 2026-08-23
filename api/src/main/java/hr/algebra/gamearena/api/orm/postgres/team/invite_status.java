package hr.algebra.gamearena.api.orm.postgres.team;

import hr.algebra.gamearena.api.model.team.InviteStatus;

public enum invite_status {
    PENDING,
    ACCEPTED,
    DECLINED,
    CANCELLED;

    public static invite_status fromInviteStatus(InviteStatus status) {
        return switch (status) {
            case PENDING -> invite_status.PENDING;
            case ACCEPTED -> invite_status.ACCEPTED;
            case DECLINED -> invite_status.DECLINED;
            case CANCELLED -> invite_status.CANCELLED;
        };
    }
}
