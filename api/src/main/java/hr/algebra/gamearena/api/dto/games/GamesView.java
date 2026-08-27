package hr.algebra.gamearena.api.dto.games;

import hr.algebra.gamearena.api.model.games.Games;

import java.util.Optional;

public record GamesView(Long id, String name, Optional<String> description, boolean isActive) {
    private static final GamesView NOT_FOUND = new GamesView(-1L, "Game not found", Optional.empty(), false);

    public static GamesView fromGamesModel(Games games) {
        return new GamesView(
                games.id(),
                games.name(),
                games.description(),
                games.isActive()
        );
    }

    public static GamesView fromGamesModelOrNotFound(Games games) {
        return games != null ? fromGamesModel(games) : NOT_FOUND;
    }
}
