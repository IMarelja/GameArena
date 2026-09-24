package hr.algebra.gamearena.api.repository.games;

import hr.algebra.gamearena.api.orm.postgres.games.GamesPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IGamesPostgreSQLRepo extends JpaRepository<GamesPostgres, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    List<GamesPostgres> findAllByIsActiveTrue(); // This is crazy that this works

    @Query("""
            SELECT m FROM GamesPostgres m
            ORDER BY
                CASE WHEN :ascending = true THEN m.createdAt END ASC,
                CASE WHEN :ascending = false THEN m.createdAt END DESC
            """)
    List<GamesPostgres> queryByDate(@Param("ascending") Boolean ascending);


}
