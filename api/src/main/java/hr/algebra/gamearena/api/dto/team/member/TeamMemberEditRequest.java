package hr.algebra.gamearena.api.dto.team.member;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberEditRequest {
    @NotNull(message = "Role is required")
    private TeamMemberRoleView role;
}
