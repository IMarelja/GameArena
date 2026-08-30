package hr.algebra.gamearena.webapp.models.mvc.data.team.invite;

import hr.algebra.gamearena.webapp.models.cereal.team.invitation.TeamInvitationDecereal;
import hr.algebra.gamearena.webapp.models.mvc.data.team.TeamJustNameViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.user.UserJustUsernameViewData;

import java.time.OffsetDateTime;
import java.util.Optional;

public record TeamInviteViewData(
        Long id,
        UserJustUsernameViewData invitee,
        UserJustUsernameViewData inviter,
        TeamJustNameViewData team,
        InviteStatusDataEnum status,
        OffsetDateTime createdAt,
        Optional<OffsetDateTime> respondedAt
) {
    public static TeamInviteViewData fromTeamInvitationDecereal(TeamInvitationDecereal decereal){
        return new TeamInviteViewData(
                decereal.id(),
                UserJustUsernameViewData.from(decereal.invitee()),
                UserJustUsernameViewData.from(decereal.inviter()),
                TeamJustNameViewData.fromTeamJustNameDecereal(decereal.team()),
                InviteStatusDataEnum.fromInvite(decereal.status()),
                decereal.createdAt(),
                decereal.respondedAt()
        );
    }
}
