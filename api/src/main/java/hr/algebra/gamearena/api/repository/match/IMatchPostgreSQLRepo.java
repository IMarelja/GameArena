package hr.algebra.gamearena.api.repository.match;

import hr.algebra.gamearena.api.orm.postgres.match.MatchPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IMatchPostgreSQLRepo extends JpaRepository<MatchPostgres, Long> {
    List<MatchPostgres> findByTournamentId(Long tournamentId);
    List<MatchPostgres> findByPlayerOneIdOrPlayerTwoId(Long playerOneId, Long playerTwoId);

    @Query("""
            SELECT m FROM MatchPostgres m
            WHERE (:gameId IS NULL OR m.gameId = :gameId)
            ORDER BY
                CASE WHEN :ascending = true THEN m.createdAt END ASC,
                CASE WHEN :ascending = false THEN m.createdAt END DESC
            """)
    List<MatchPostgres> findMyQuery(
            @Param("gameId") Optional<Long> gameId,
            @Param("ascending") Boolean ascending
    );
}
