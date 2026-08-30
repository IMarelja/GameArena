package hr.algebra.gamearena.webapp.models.mvc.data.team.invite;

public record TeamInviterUpdateViewData(
        TeamInviteViewData invite,
        UpdateInviterStatusView status
) {
}
