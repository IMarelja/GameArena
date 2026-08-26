package hr.algebra.gamearena.webapp.models.mvc.data.user;

import hr.algebra.gamearena.webapp.models.cereal.leaderboard.TournamentStatsEntryViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchDetailFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.user.UserViewDtoDecereal;

import java.util.List;

public record UserProfileViewData(
        UserViewDtoDecereal user,
        List<TournamentStatsEntryViewDecereal> leaderboard,
        List<TournamentFullViewDecereal> tournaments,
        List<MatchDetailFullViewDecereal> matches
) {
}
