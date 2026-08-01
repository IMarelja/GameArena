package hr.algebra.gamearena.api.dto.games;

import hr.algebra.gamearena.api.model.games.Games;

import java.util.Optional;

public record GamesView(Long id, String name, Optional<String> description, boolean isActive) {
    public static GamesView toGamesView(Games games) {
        return new GamesView(
                games.id(),
                games.name(),
                games.description(),
                games.isActive()
        );
    }
}
