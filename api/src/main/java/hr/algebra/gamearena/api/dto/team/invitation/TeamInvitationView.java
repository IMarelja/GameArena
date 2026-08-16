package hr.algebra.gamearena.api.dto.team.invitation;

import hr.algebra.gamearena.api.model.team.InviteStatus;
import hr.algebra.gamearena.api.model.team.TeamInvitation;

import java.time.LocalDateTime;

public record TeamInvitationView(
    Long id,
    Long teamId,
    Long inviterId,
    Long inviteeId,
    InviteStatus status,
    LocalDateTime createdAt,
    LocalDateTime respondedAt
) {
    public static TeamInvitationView fromTeamInvitation(TeamInvitation invitation) {
        return new TeamInvitationView(
                invitation.id(),
                invitation.teamId(),
                invitation.inviterId(),
                invitation.inviteeId(),
                invitation.status(),
                invitation.createdAt(),
                invitation.respondedAt()
        );
    }
}
