package hr.algebra.gamearena.api.model.team;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamUpdate {
    private String name;
    private Long gameId;
}
