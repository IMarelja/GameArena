package hr.algebra.gamearena.api.model.team;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberSave {
    private Long teamId;
    private Long userId;
}
