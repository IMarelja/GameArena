package hr.algebra.gamearena.api.model.games;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GamesUpdate {
    private String name;
    private String description;
    private boolean isActive;
}
