package hr.algebra.gamearena.api.dto.team.invitation;

import hr.algebra.gamearena.api.model.team.InviteStatus;

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
}
