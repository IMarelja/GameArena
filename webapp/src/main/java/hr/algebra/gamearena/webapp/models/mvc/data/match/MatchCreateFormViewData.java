package hr.algebra.gamearena.webapp.models.mvc.data.match;

import hr.algebra.gamearena.webapp.models.mvc.data.tournament.member.TournamentMemberViewData;

import java.util.List;

public record MatchCreateFormViewData(
        Long tournamentId,
        MatchCreatePostViewModel form,
        List<TournamentMemberViewData> members
) {
}
