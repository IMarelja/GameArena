package hr.algebra.gamearena.webapp.models.mvc.data.tournament;

import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewData;

import java.util.List;
import java.util.Optional;

public record TournamentEditFormViewData(
        Long tournamentId,
        TournamentEditPostViewModel form,
        Optional<List<GameViewData>> activeGames
) {
}
