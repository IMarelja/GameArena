package hr.algebra.gamearena.api.repository.tournament;

import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;
import hr.algebra.gamearena.api.orm.postgres.tournament.TournamentMemberPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITournamentMemberPostgreSQLRepo extends JpaRepository<TournamentMemberPostgres, Long> {
    List<TournamentMemberPostgres> findByTournamentId(Long tournamentId);
    List<TournamentMemberPostgres> findByTournamentIdAndUserId(Long tournamentId, Long userId);
    boolean existsByTournamentIdAndUserId(Long tournamentId, Long userId);
    boolean existsByTournamentIdAndUserIdAndConfirmedTrue(Long tournamentId, Long userId);
    long countByTournamentIdAndRole(Long tournamentId, TournamentMemberRole role);
}
