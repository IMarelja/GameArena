package hr.algebra.gamearena.webapp.models.cereal.team.invitation;

import com.gamearena.client.model.TeamInvitationResponseEditRequest;

public record TeamInvitationInviteeRespondCereal(
        StatusInviteeCereal status
) {
    public TeamInvitationResponseEditRequest toTeamInvitationResponseEditRequest() {
        var request = new TeamInvitationResponseEditRequest();
        request.setStatus(StatusInviteeCereal.toStatusEnum(status));
        return request;
    }
}
