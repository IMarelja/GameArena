package hr.algebra.gamearena.api.repository.tournament;

import hr.algebra.gamearena.api.orm.postgres.TournamentMemberPostgres;
import hr.algebra.gamearena.api.orm.postgres.TournamentPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITournamentPostgreSQLRepo extends JpaRepository<TournamentMemberPostgres, Long> {
}
