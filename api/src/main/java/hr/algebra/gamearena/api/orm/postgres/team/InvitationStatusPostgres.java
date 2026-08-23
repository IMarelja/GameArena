package hr.algebra.gamearena.api.orm.postgres.team;

import hr.algebra.gamearena.api.model.team.InviteStatus;

public enum InvitationStatusPostgres {
    PENDING,
    ACCEPTED,
    DECLINED,
    CANCELLED;

    public static InvitationStatusPostgres fromInviteStatus(InviteStatus status) {
        return switch (status) {
            case PENDING -> InvitationStatusPostgres.PENDING;
            case ACCEPTED -> InvitationStatusPostgres.ACCEPTED;
            case DECLINED -> InvitationStatusPostgres.DECLINED;
            case CANCELLED -> InvitationStatusPostgres.CANCELLED;
        };
    }
}
