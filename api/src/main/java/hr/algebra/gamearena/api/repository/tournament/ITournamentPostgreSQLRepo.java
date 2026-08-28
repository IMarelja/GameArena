package hr.algebra.gamearena.api.repository.tournament;

import hr.algebra.gamearena.api.orm.postgres.tournament.TournamentPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITournamentPostgreSQLRepo extends JpaRepository<TournamentPostgres, Long> {

    @Query("""
            SELECT t FROM TournamentPostgres t
            WHERE (:gameId IS NULL OR t.gameId = :gameId)
            ORDER BY
                CASE WHEN :ascending = true THEN t.createdAt END ASC,
                CASE WHEN :ascending = false THEN t.createdAt END DESC
            """)
    List<TournamentPostgres> findAllByOptionalGameIdOrderByCreatedAt(
            @Param("gameId") Optional<Long> gameId,
            @Param("ascending") Boolean ascending
    );
}
