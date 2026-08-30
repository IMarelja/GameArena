package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.orm.postgres.team.TeamInvitationPostgres;
import hr.algebra.gamearena.api.orm.postgres.team.invite_status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITeamInvitationPostgreSQLRepo extends JpaRepository<TeamInvitationPostgres, Long> {
    boolean existsByTeamIdAndInviteeIdAndStatus(Long teamId, Long inviteeId, invite_status status);
    boolean existsByInvitationIdAndInviteeId(Long invitationId, Long inviteeId);
    boolean existsByInvitationIdAndInviterId(Long invitationId, Long inviterId);
    List<TeamInvitationPostgres> findByInviteeIdOrInviterId(Long inviteeId, Long inviterId);
}
