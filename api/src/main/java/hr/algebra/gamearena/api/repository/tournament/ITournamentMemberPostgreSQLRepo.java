package hr.algebra.gamearena.api.repository.tournament;

import hr.algebra.gamearena.api.orm.postgres.TournamentMemberPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITournamentMemberPostgreSQLRepo extends JpaRepository<TournamentMemberPostgres, Long> {
    List<TournamentMemberPostgres> findByTournamentId(Long tournamentId);
    boolean existsByTournamentIdAndUserId(Long tournamentId, Long userId);
}
