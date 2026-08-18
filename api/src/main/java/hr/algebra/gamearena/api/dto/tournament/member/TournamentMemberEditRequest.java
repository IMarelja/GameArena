package hr.algebra.gamearena.api.dto.tournament.member;

import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentMemberEditRequest {
    @NotNull(message = "Selecting a tournament is required")
    private Long tournamentId;

    @NotNull(message = "Select a role for the user")
    private TournamentMemberRole role;
}
