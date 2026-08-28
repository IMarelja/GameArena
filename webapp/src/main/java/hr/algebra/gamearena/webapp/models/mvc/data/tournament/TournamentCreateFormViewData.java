package hr.algebra.gamearena.webapp.models.mvc.data.tournament;

import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewsData;

import java.util.List;

public record TournamentCreateFormViewData(
        TournamentCreatePostViewModel form,
        List<GameViewsData> activeGames
) {
}
