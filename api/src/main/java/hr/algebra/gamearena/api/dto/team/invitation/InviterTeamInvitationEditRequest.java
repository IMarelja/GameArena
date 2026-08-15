package hr.algebra.gamearena.api.dto.team.invitation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviterTeamInvitationEditRequest {
    private InviterInviteStatus status;
}
