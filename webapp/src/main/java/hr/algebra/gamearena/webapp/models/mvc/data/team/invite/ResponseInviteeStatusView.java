package hr.algebra.gamearena.webapp.models.mvc.data.team.invite;

import hr.algebra.gamearena.webapp.models.cereal.team.invitation.StatusInviteeCereal;

public enum ResponseInviteeStatusView {
    ACCEPT,
    DECLINE;

    public  StatusInviteeCereal toStatusInviteeCereal() {
        return switch (this){
            case ACCEPT -> StatusInviteeCereal.ACCEPT;
            case DECLINE -> StatusInviteeCereal.DECLINE;
        };
    }
}
