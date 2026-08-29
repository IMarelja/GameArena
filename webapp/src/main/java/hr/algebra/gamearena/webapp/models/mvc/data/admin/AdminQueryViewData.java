package hr.algebra.gamearena.webapp.models.mvc.data.admin;

import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentViewData;

import java.util.List;
import java.util.Optional;

public record AdminQueryViewData(
        AdminQueryGetViewModel form,
        Optional<List<GameViewData>> gamesAll,
        Optional<List<TournamentViewData>> tournaments,
        Optional<List<MatchViewData>> matches
) {
}
