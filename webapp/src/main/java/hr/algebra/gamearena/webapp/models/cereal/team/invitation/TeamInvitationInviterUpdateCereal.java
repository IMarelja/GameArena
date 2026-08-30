package hr.algebra.gamearena.webapp.models.cereal.team.invitation;

import com.gamearena.client.model.InviterTeamInvitationEditRequest;

public record TeamInvitationInviterUpdateCereal(
    StatusInviterCereal status
) {
    public InviterTeamInvitationEditRequest toInviterTeamInvitationEditRequest() {
        var request = new InviterTeamInvitationEditRequest();
        request.setStatus(StatusInviterCereal.toStatusEnum(status));
        return request;
    }
}
