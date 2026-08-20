package hr.algebra.gamearena.api.model.tournament.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TournamentMemberUpdate {
    private TournamentMemberRole role;
    private Long groupId;
}
