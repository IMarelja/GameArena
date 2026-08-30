package hr.algebra.gamearena.webapp.models.mvc.data.team.invite;

import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentViewData;

import java.util.List;

public record TeamInviteCreateViewData(
        List<TournamentViewData> partOfTeams
) {
}
