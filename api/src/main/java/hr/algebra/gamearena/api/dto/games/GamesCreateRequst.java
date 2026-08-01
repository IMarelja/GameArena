package hr.algebra.gamearena.api.dto.games;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class GamesCreateRequst {
    private String name;
    private Optional<String> description;
}
