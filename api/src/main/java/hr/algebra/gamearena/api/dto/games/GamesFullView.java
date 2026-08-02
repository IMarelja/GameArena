package hr.algebra.gamearena.api.dto.games;

import hr.algebra.gamearena.api.model.games.Games;

import java.time.LocalDateTime;
import java.util.Optional;

public record GamesFullView(Long id, String name, Optional<String> description, boolean isActive, LocalDateTime createAt) {
    public static GamesFullView fromGamesModel(Games games) {
        return new GamesFullView(
                games.id(),
                games.name(),
                games.description(),
                games.isActive(),
                games.createAt()
        );
    }
}
