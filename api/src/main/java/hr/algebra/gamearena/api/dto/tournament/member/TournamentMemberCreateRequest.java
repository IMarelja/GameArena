package hr.algebra.gamearena.api.dto.tournament.member;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentMemberCreateRequest {
    @NotNull(message = "You must select a user")
    private Long userId;

    @NotNull(message = "Select a role for the user")
    private TournamentMemberRoleCreate role;
}
