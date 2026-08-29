package hr.algebra.gamearena.webapp.models.mvc.data.team;

import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewData;

import java.util.List;
import java.util.Optional;

public record TeamAddFormViewData(
        TeamAddPostViewModel form,
        Optional<List<GameViewData>> activeGames
) {
}
