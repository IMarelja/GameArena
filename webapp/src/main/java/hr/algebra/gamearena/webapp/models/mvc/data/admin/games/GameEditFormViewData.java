package hr.algebra.gamearena.webapp.models.mvc.data.admin.games;

import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewData;

import java.util.List;
import java.util.Optional;

public record GameEditFormViewData(
        Long gameId,
        GameEditPostViewModel form,
        Optional<List<GameViewData>> allGames
) {
}
