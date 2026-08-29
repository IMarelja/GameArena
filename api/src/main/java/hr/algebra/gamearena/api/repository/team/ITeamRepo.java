package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.model.team.*;

import java.util.List;
import java.util.Optional;

public interface ITeamRepo {

    /** TEAM */
    // BEGIN
    List<Team> getAll();
    Optional<Team> getTeamById(Long id);
    List<Team> getTeamsForUserId(Long userId);
    Team saveTeamInvite(TeamSave team);
    Optional<Team> updateTeam(Long id, TeamUpdate teamUpdate);
    void deleteTeam(Long id);
    boolean doesTeamExist(Long id);
    // END

    /** TEAM - TEAM MEMBER */
    // BEGIN
    Long memberCountInATeam(Long teamId);
    long countTeamMembersByRole(Long teamId, TeamMemberRole role);
    boolean isUserIdPartOfTeam(Long userId, Long teamId);
    boolean isUserTeamCaptain(Long userId, Long teamId);
    // END

    /** TEAM MEMBER */
    Optional<TeamMember> getTeamMemberByTeamIdAndUserId(Long teamId, Long userId);
    Optional<TeamMember> getTeamMemberById(Long teamMemberId);
    void addMember(TeamMemberSave save);
    Optional<TeamMember> updateTeamMember(Long teamMemberId, TeamMemberUpdate teamMemberUpdate);
    void deleteMember(Long teamId, Long userId);
    void deleteTeamMember(Long teamMemberId);
    List<TeamMember> getTeamMembers(Long teamId);

    /** TEAM INVITATION */
    // BEGIN
    Optional<TeamInvitation> getInvitationById(Long id);
    List<TeamInvitation> getInvitationsForUserId(Long userId);
    TeamInvitation saveTeamInvite(TeamInvitationSave save);
    Optional<TeamInvitation> updateTeamInvite(Long id, TeamInvitationUpdate update);
    boolean isUserAnInviteeOfInvitation(Long invitationId, Long userId);
    boolean isUserAnInviterOfInvitation(Long invitationId, Long userId);
    boolean doesPendingTeamInvitationExist(Long teamId, Long inviteeId);
    // END
}
