package hr.algebra.gamearena.webapp.models.mvc.data.games;

import hr.algebra.gamearena.webapp.models.cereal.games.GamesViewDecereal;

import java.util.Optional;

public record GameViewsData(
        Long id,
        String name,
        Optional<String> description,
        Boolean isActive
) {

    public static GameViewsData fromGamesViewDecereal(GamesViewDecereal gamesViewDecereal) {
        return new GameViewsData(
                gamesViewDecereal.id(),
                gamesViewDecereal.name(),
                Optional.ofNullable(gamesViewDecereal.description()),
                gamesViewDecereal.isActive()
        );
    }
}
