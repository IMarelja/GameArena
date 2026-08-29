package hr.algebra.gamearena.webapp.models.mvc.data.tournament;

import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewData;

import java.util.List;

public record TournamentCreateFormViewData(
        TournamentCreatePostViewModel form,
        List<GameViewData> activeGames
) {
}
