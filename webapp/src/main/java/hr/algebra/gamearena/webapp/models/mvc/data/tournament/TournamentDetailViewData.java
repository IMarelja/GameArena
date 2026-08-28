package hr.algebra.gamearena.webapp.models.mvc.data.tournament;

import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.member.TournamentMemberViewData;

import java.util.List;
import java.util.Optional;

public record TournamentDetailViewData(
        TournamentViewData tournament,
        Optional<List<TournamentMemberViewData>> members,
        Optional<List<MatchViewData>> matches,
        Optional<TournamentMemberViewData> meMember
) {
}
