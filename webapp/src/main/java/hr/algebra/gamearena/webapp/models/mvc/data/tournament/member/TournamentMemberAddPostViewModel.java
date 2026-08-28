package hr.algebra.gamearena.webapp.models.mvc.data.tournament.member;

import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberCreateCereal;
import jakarta.validation.constraints.NotNull;

public record TournamentMemberAddPostViewModel(
        @NotNull(message = "A tournament must be selected")
        Long tournamentId,

        @NotNull(message = "Role is required")
        TournamentMemberRoleCreateViewEnum role
) {
    public TournamentMemberCreateCereal toTournamentMemberCreateCereal(Long userId) {
        return new TournamentMemberCreateCereal(userId, role);
    }
}
