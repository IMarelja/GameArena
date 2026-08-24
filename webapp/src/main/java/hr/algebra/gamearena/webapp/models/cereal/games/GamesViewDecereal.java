package hr.algebra.gamearena.webapp.models.cereal.games;

import com.gamearena.client.model.GamesView;

public record GamesViewDecereal(
        Long id,
        String name,
        String description,
        Boolean isActive
) {
    public static GamesViewDecereal fromGamesViewClient(GamesView games) {
        return new GamesViewDecereal(games.getId(), games.getName(), games.getDescription(), games.getIsActive());
    }
}
