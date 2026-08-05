package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.orm.postgres.TeamMemberPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITeamMemberPostgreSQLRepo extends JpaRepository<TeamMemberPostgres, Long> {
    long countByTeamId(Long teamId);
}
