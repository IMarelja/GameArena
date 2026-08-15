package hr.algebra.gamearena.api.model.team;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamInvitationSave {
    public Long teamId;
    public Long inviterId;
    public Long inviteeId;
    public InviteStatus status;
}
