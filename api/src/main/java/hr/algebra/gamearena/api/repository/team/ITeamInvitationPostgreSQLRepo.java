package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.model.team.InviteStatus;
import hr.algebra.gamearena.api.orm.postgres.team.TeamInvitationPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITeamInvitationPostgreSQLRepo extends JpaRepository<TeamInvitationPostgres, Long> {
    boolean existsByTeamIdAndInviteeIdAndStatus(Long teamId, Long inviteeId, InviteStatus status);
    boolean existsByInvitationIdAndInviteeId(Long invitationId, Long inviteeId);
    boolean existsByInvitationIdAndInviterId(Long invitationId, Long inviterId);
}
