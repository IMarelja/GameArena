package hr.algebra.gamearena.api.repository.match;

import hr.algebra.gamearena.api.orm.postgres.match.MatchPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IMatchPostgreSQLRepo extends JpaRepository<MatchPostgres, Long> {
    List<MatchPostgres> findByTournamentId(Long tournamentId);
    List<MatchPostgres> findByPlayerOneIdOrPlayerTwoId(Long playerOneId, Long playerTwoId);
}
