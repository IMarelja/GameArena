package hr.algebra.gamearena.api.model.team;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamInvitationSave {
    private Long teamId;
    private Long inviterId;
    private Long inviteeId;
    private InviteStatus status;
}
