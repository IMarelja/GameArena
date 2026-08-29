package hr.algebra.gamearena.webapp.models.cereal.games;

import com.gamearena.client.model.GamesEditRequest;

public record GameEditCereal(
        String name,
        String description,
        Boolean isActive
) {
    public GamesEditRequest toGamesEditRequest() {
        GamesEditRequest request = new GamesEditRequest();
        request.setName(name);
        request.setDescription(description);
        request.setIsActive(isActive);
        return request;
    }
}
