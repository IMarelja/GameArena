package hr.algebra.gamearena.webapp.models.cereal.team.invitation;

import com.gamearena.client.model.TeamInvitationResponseEditRequest;

public enum StatusInviteeCereal {
    ACCEPT,
    DECLINE;

    public static TeamInvitationResponseEditRequest.StatusEnum toStatusEnum(StatusInviteeCereal cereal) {
        return switch (cereal) {
            case ACCEPT -> TeamInvitationResponseEditRequest.StatusEnum.ACCEPT;
            case DECLINE -> TeamInvitationResponseEditRequest.StatusEnum.DECLINE;
        };
    }
}
