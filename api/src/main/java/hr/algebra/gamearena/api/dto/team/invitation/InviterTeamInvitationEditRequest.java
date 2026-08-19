package hr.algebra.gamearena.api.dto.team.invitation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviterTeamInvitationEditRequest {
    @NotNull(message = "Status is required")
    private InviterInviteStatus status;
}
