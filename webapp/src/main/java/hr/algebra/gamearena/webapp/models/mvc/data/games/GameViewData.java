package hr.algebra.gamearena.webapp.models.mvc.data.games;

import hr.algebra.gamearena.webapp.models.cereal.games.GamesViewDecereal;

import java.util.Optional;

public record GameViewData(
        Long id,
        String name,
        Optional<String> description,
        Boolean isActive
) {

    public static GameViewData fromGamesViewDecereal(GamesViewDecereal gamesViewDecereal) {
        return new GameViewData(
                gamesViewDecereal.id(),
                gamesViewDecereal.name(),
                Optional.ofNullable(gamesViewDecereal.description()),
                gamesViewDecereal.isActive()
        );
    }
}
