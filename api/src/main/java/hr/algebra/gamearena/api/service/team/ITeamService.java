package hr.algebra.gamearena.api.service.team;

import hr.algebra.gamearena.api.dto.team.TeamCreateRequest;
import hr.algebra.gamearena.api.dto.team.TeamMinimalView;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationResponseEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.InviterTeamInvitationEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationView;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface ITeamService {
    List<TeamMinimalView> getAll();
    Optional<TeamMinimalView> getTeamById(@PathVariable Long id);
    TeamMinimalView createTeamByUsersRequest(Long userId, TeamCreateRequest team);
    TeamInvitationView createInvitationAndPushNotification(Long inviterId, Long teamId, Long userId);
    TeamInvitationView respondInvitationAndPushNotification(Long invitationId, TeamInvitationResponseEditRequest request);
    TeamInvitationView updateInvitationAndPushNotification(Long invitationId, InviterTeamInvitationEditRequest request);
    boolean isUserIdPartOfTeam(Long userId, Long teamId);
    boolean isUserAnInviteeOfInvitation(Long invitationId, Long userId);
    boolean isUserAnInviterOfInvitation(Long invitationId, Long userId);
}
