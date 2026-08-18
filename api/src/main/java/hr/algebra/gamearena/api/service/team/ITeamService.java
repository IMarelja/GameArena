package hr.algebra.gamearena.api.service.team;

import hr.algebra.gamearena.api.dto.team.TeamCreateRequest;
import hr.algebra.gamearena.api.dto.team.TeamMinimalView;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationResponseEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.InviterTeamInvitationEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationView;
import hr.algebra.gamearena.api.dto.team.member.TeamMemberMinimalView;

import java.util.List;
import java.util.Optional;

public interface ITeamService {
    // Team
    List<TeamMinimalView> getAll();
    Optional<TeamMinimalView> getTeamById(Long id);
    List<TeamMinimalView> getTeamsForUser(Long userId);
    TeamMinimalView createTeamByUsersRequest(Long userId, TeamCreateRequest team);

    // Team invitation
    Optional<TeamInvitationView> getInvitationById(Long callerUserId, Long id);
    TeamInvitationView createInvitationAndPushNotification(Long inviterId, Long teamId, Long userId);
    TeamInvitationView respondInvitationAndPushNotification(Long invitationId, Long callerId, TeamInvitationResponseEditRequest request);
    TeamInvitationView updateInvitationAndPushNotification(Long invitationId, Long callerId, InviterTeamInvitationEditRequest request);

    // Team member
    List<TeamMemberMinimalView> getTeamMembers(Long teamId);
    void removeTeamMember(Long callerId, Long teamId, Long teamMemberId);
    void leaveTeam(Long callerId, Long teamId);
    boolean isUserPartOfTeam(Long callerId, Long teamId);
}
