package hr.algebra.gamearena.api.dto.team.invitation;

import hr.algebra.gamearena.api.dto.team.TeamJustNameDto;
import hr.algebra.gamearena.api.dto.user.UserJustUsernameView;
import hr.algebra.gamearena.api.model.team.Team;
import hr.algebra.gamearena.api.model.team.TeamInvitation;
import hr.algebra.gamearena.api.model.user.User;

import java.time.LocalDateTime;

public record TeamInvitationView(
    Long id,
    TeamJustNameDto team,
    UserJustUsernameView inviter,
    UserJustUsernameView invitee,
    InviteStatusView status,
    LocalDateTime createdAt,
    LocalDateTime respondedAt
) {
    public static TeamInvitationView fromTeamInvitationTeamUserInviteeAndUserInviter(TeamInvitation invitation, Team team, User inviter, User invitee) {
        return new TeamInvitationView(
                invitation.id(),
                TeamJustNameDto.fromTeam(team),
                UserJustUsernameView.fromUser(inviter),
                UserJustUsernameView.fromUser(invitee),
                InviteStatusView.fromInviteStatus(invitation.status()),
                invitation.createdAt(),
                invitation.respondedAt()
        );
    }
}
