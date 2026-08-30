package hr.algebra.gamearena.webapp.models.mvc.data.team.invite;

import hr.algebra.gamearena.webapp.models.cereal.team.invitation.StatusInviterCereal;

public enum UpdateInviterStatusView {
    CANCELLED;

    public StatusInviterCereal toCereal() {
        return switch (this){
            case CANCELLED -> StatusInviterCereal.CANCEL;
        };
    }
}
