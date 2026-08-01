package hr.algebra.gamearena.api.model.games;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class GamesUpdate {
    private String name;
    private Optional<String> description;
    private boolean isActive;
}
