package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.model.team.*;

import java.util.List;
import java.util.Optional;

public interface ITeamRepo {
    List<Team> getAll();
    Optional<Team> getTeamById(Long id);
    Long memberCountInATeam(Long teamId);
    Team save(TeamSave team);
    TeamInvitation save(TeamInvitationSave save);
    Optional<TeamInvitation> update(Long id, TeamInvitationUpdate update);
    void deleteTeam(Long id);
    boolean doesTeamExist(Long id);
    boolean doesTeamInvitationExist(Team team);
    boolean isUserIdPartOfTeam(Long userId, Long teamId);
    boolean isUserAnInviteeOfInvitation(Long invitationId, Long userId);
    boolean isUserAnInviterOfInvitation(Long invitationId, Long userId);
}
