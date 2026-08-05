package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.orm.postgres.TeamInvitationPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITeamInvitationPostgreSQLRepo extends JpaRepository<TeamInvitationPostgres, Long> {
}
