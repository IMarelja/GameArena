package hr.algebra.gamearena.webapp.models.mvc.data.user;

import hr.algebra.gamearena.webapp.models.mvc.data.leaderboard.TournamentStatsEntryViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentViewData;

import java.util.List;
import java.util.Optional;

public record MeProfileViewData(
        UserFullViewData user,
        Optional<List<TournamentStatsEntryViewData>> leaderboard,
        Optional<List<TournamentViewData>> tournaments,
        Optional<List<MatchViewData>> matches
) {
}
