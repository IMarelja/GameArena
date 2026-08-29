package hr.algebra.gamearena.api.service.team;

import hr.algebra.gamearena.api.dto.team.TeamCreateRequest;
import hr.algebra.gamearena.api.dto.team.TeamEditRequest;
import hr.algebra.gamearena.api.dto.team.TeamMinimalView;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationResponseEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.InviterTeamInvitationEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationView;
import hr.algebra.gamearena.api.dto.team.member.TeamMemberEditRequest;
import hr.algebra.gamearena.api.dto.team.member.TeamMemberFullView;
import hr.algebra.gamearena.api.dto.team.member.TeamMemberMinimalView;

import java.util.List;
import java.util.Optional;

public interface ITeamService {
    // Team
    List<TeamMinimalView> getAll();
    Optional<TeamMinimalView> getTeamById(Long id);
    List<TeamMinimalView> getTeamsForUser(Long userId);
    TeamMinimalView createTeamByUsersRequest(Long userId, TeamCreateRequest team);
    TeamMinimalView editTeam(Long callerId, Long teamId, TeamEditRequest request);

    // Team invitation
    Optional<TeamInvitationView> getInvitationById(Long callerUserId, Long id);
    List<TeamInvitationView> getInvitationsForUser(Long callerUserId);
    TeamInvitationView createInvitationAndPushNotification(Long inviterId, Long teamId, Long userId);
    TeamInvitationView respondInvitationAndPushNotification(Long invitationId, Long callerId, TeamInvitationResponseEditRequest request);
    TeamInvitationView updateInvitationAndPushNotification(Long invitationId, Long callerId, InviterTeamInvitationEditRequest request);

    // Team member
    Optional<TeamMemberFullView> getTeamMemberByUserIdAndTeamId(Long userId, Long teamId);
    Optional<TeamMemberFullView> getTeamMemberByIdAndTeamId(Long id, Long teamId);
    List<TeamMemberMinimalView> getTeamMembers(Long teamId);
    TeamMemberFullView editTeamMemberRole(Long callerId, Long teamId, Long memberId, TeamMemberEditRequest request);
    void removeTeamMember(Long callerId, Long teamId, Long teamMemberId);
    void leaveTeam(Long callerId, Long teamId);
}
