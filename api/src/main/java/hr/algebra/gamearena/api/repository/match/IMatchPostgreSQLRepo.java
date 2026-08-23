package hr.algebra.gamearena.api.repository.match;

import hr.algebra.gamearena.api.orm.postgres.match.MatchPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMatchPostgreSQLRepo extends JpaRepository<MatchPostgres, Long> {
}
