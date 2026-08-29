package hr.algebra.gamearena.webapp.models.cereal.team.invitation;

import com.gamearena.client.model.InviterTeamInvitationEditRequest;

public enum StatusInviterCereal {
    CANCEL;

    public static InviterTeamInvitationEditRequest.StatusEnum toStatusEnum(StatusInviterCereal cereal){
        return switch (cereal) {
            case CANCEL -> InviterTeamInvitationEditRequest.StatusEnum.CANCEL;
        };
    }
}
