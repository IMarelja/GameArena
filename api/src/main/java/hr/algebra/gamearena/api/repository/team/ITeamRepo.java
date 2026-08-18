package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.model.team.*;

import java.util.List;
import java.util.Optional;

public interface ITeamRepo {
    List<Team> getAll();
    Optional<Team> getTeamById(Long id);
    List<Team> getTeamsForUser(Long userId);
    Long memberCountInATeam(Long teamId);
    Team save(TeamSave team);
    TeamInvitation save(TeamInvitationSave save);
    Optional<TeamInvitation> getInvitationById(Long id);
    Optional<TeamInvitation> update(Long id, TeamInvitationUpdate update);
    void addMember(TeamMemberSave save);
    void deleteMember(Long teamId, Long userId);
    void deleteTeamMember(Long teamMemberId);
    List<TeamMember> getTeamMembers(Long teamId);
    void deleteTeam(Long id);
    boolean doesTeamExist(Long id);
    boolean doesPendingTeamInvitationExist(Long teamId, Long inviteeId);
    boolean isUserIdPartOfTeam(Long userId, Long teamId);
    boolean isUserTeamCaptain(Long userId, Long teamId);
    long countTeamMembersByRole(Long teamId, TeamMemberRole role);
    Optional<TeamMember> getTeamMember(Long teamId, Long userId);
    Optional<TeamMember> getTeamMemberById(Long teamMemberId);
    boolean isUserAnInviteeOfInvitation(Long invitationId, Long userId);
    boolean isUserAnInviterOfInvitation(Long invitationId, Long userId);
}
