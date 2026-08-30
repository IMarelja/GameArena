package hr.algebra.gamearena.webapp.models.mvc.data.team.invite;

public record TeamInviteeResponseViewData(
        TeamInviteViewData invite,
        ResponseInviteeStatusView status
) {
}
