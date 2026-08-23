package hr.algebra.gamearena.api.repository.tournament;

import hr.algebra.gamearena.api.orm.postgres.tournament.TournamentMemberPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITournamentMemberPostgreSQLRepo extends JpaRepository<TournamentMemberPostgres, Long> {
    List<TournamentMemberPostgres> findByTournamentId(Long tournamentId);
    List<TournamentMemberPostgres> findByUserId(Long userId);
    List<TournamentMemberPostgres> findByTournamentIdAndUserId(Long tournamentId, Long userId);
    boolean existsByTournamentIdAndUserId(Long tournamentId, Long userId);
    boolean existsByTournamentIdAndUserIdAndConfirmedTrue(Long tournamentId, Long userId);
    boolean existsByTournamentIdAndUserIdAndConfirmedTrueAndRole(Long tournamentId, Long userId, String role);
    long countByTournamentIdAndRole(Long tournamentId, String role);
}
