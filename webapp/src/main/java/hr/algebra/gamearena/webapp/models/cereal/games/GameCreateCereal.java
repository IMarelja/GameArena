package hr.algebra.gamearena.webapp.models.cereal.games;

import com.gamearena.client.model.GamesCreateRequest;

public record GameCreateCereal(
        String name,
        String description
) {
    public GamesCreateRequest toGamesCreateRequest() {
        GamesCreateRequest request = new GamesCreateRequest();

        request.setName(name);
        request.setDescription(description);

        return request;
    }
}
