package hr.algebra.gamearena.webapp.models.mvc.data.match;

import hr.algebra.gamearena.webapp.models.mvc.data.tournament.member.TournamentMemberViewData;

import java.util.List;
import java.util.Optional;

public record MatchEditFormViewData(
        Long tournamentId,
        Long matchId,
        MatchEditPostViewModel form,
        Optional<List<TournamentMemberViewData>> members
) {
}
