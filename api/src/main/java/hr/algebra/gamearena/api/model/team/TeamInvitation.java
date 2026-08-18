package hr.algebra.gamearena.api.model.team;

import hr.algebra.gamearena.api.orm.postgres.TeamInvitationPostgres;

import java.time.LocalDateTime;

public record TeamInvitation(
        Long id,
        Long teamId,
        Long inviterId,
        Long inviteeId,
        InviteStatus status,
        LocalDateTime createdAt,
        LocalDateTime respondedAt
) {
    public static TeamInvitation fromTeamInvitationPostgres(TeamInvitationPostgres teamInvitationPostgres) {
        return new TeamInvitation(
                teamInvitationPostgres.getInvitationId(),
                teamInvitationPostgres.getTeamId(),
                teamInvitationPostgres.getInviterId(),
                teamInvitationPostgres.getInviteeId(),
                teamInvitationPostgres.getStatus(),
                teamInvitationPostgres.getCreatedAt().toLocalDateTime(),
                teamInvitationPostgres.getRespondedAt() != null
                        ? teamInvitationPostgres.getRespondedAt().toLocalDateTime()
                        : null
        );
    }
}
