package hr.algebra.gamearena.webapp.models.mvc.data.tournament.member;

import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberEditCereal;
import jakarta.validation.constraints.NotNull;

public record TournamentMemberEditPostViewModel(
        @NotNull(message = "Role is required")
        TournamentMemberRoleEditViewEnum role
) {
    public TournamentMemberEditCereal toTournamentMemberEditCereal() {
        return new TournamentMemberEditCereal(role);
    }
}
