package hr.algebra.gamearena.api.model.tournament.member;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TournamentMemberSave {
    private Long userId;
    private Long tournamentId;
    private TournamentMemberRole role;
}
