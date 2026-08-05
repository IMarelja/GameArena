package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.orm.postgres.TeamPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITeamPostgreSQLRepo extends JpaRepository<TeamPostgres, Long> {
}
