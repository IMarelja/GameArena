package hr.algebra.gamearena.webapp.models.cereal.games;

import com.gamearena.client.model.GamesFullView;
import com.gamearena.client.model.GamesView;

public record GamesViewDecereal(
        Long id,
        String name,
        String description,
        Boolean isActive
) {
    private static final GamesViewDecereal UNAVAILABLE = new GamesViewDecereal(
            -1L,
            "Unknown Game",
            "This game's data could not be loaded",
            false
    );

    public static GamesViewDecereal fromGamesFullViewClient(GamesFullView games) {
        return new GamesViewDecereal(
                games.getId(),
                games.getName(),
                games.getDescription(),
                games.getIsActive()
        );
    }

    public static GamesViewDecereal fromGamesViewClient(GamesView games) {
        return new GamesViewDecereal(
                games.getId(),
                games.getName(),
                games.getDescription(),
                games.getIsActive()
        );
    }

    public static GamesViewDecereal fromGamesViewClientOrUnavailableGarbage(GamesView games) {
        return games != null ? fromGamesViewClient(games) : UNAVAILABLE;
    }
}
