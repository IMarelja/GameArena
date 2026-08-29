package hr.algebra.gamearena.webapp.models.cereal.team.invitation;

import com.gamearena.client.model.TeamInvitationView;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamJustNameDecereal;
import hr.algebra.gamearena.webapp.models.cereal.user.UserJustUsernameDecereal;

import java.time.OffsetDateTime;
import java.util.Optional;

public record TeamInvitationDecereal(
        Long id,
        TeamJustNameDecereal team,
        UserJustUsernameDecereal inviter,
        UserJustUsernameDecereal invitee,
        InviteStatusDecereal status,
        OffsetDateTime createdAt,
        Optional<OffsetDateTime> respondedAt
) {
    public static TeamInvitationDecereal fromTeamInvitationViewClient(TeamInvitationView invitation) {
        return new TeamInvitationDecereal(
                invitation.getId(),
                TeamJustNameDecereal.fromTeamJustNameClientOrUnavailableGarbage(invitation.getTeam()),
                UserJustUsernameDecereal.fromUserJustUsernameClientOrUnavailableGarbage(invitation.getInviter()),
                UserJustUsernameDecereal.fromUserJustUsernameClientOrUnavailableGarbage(invitation.getInvitee()),
                InviteStatusDecereal.fromTeamInvitationViewStatusClient(invitation.getStatus()),
                invitation.getCreatedAt(),
                Optional.ofNullable(invitation.getRespondedAt())
        );
    }
}
