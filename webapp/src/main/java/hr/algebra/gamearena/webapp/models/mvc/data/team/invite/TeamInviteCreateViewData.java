package hr.algebra.gamearena.webapp.models.mvc.data.team.invite;

import hr.algebra.gamearena.webapp.models.mvc.data.team.TeamMinimalViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.user.UserJustUsernameViewData;

import java.util.List;

public record TeamInviteCreateViewData(
        UserJustUsernameViewData user,
        TeamInviteCreatePostView form,
        List<TeamMinimalViewData> teams
) {
}
