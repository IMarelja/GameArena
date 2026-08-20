package hr.algebra.gamearena.api.model.tournament;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TournamentGroupSave {
    private Long tournamentId;
    private String name;
    private Long createdBy;
}
