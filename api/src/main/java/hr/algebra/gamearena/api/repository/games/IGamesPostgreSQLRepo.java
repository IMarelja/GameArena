package hr.algebra.gamearena.api.repository.games;

import hr.algebra.gamearena.api.orm.postgres.GamesPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IGamesPostgreSQLRepo extends JpaRepository<GamesPostgres, Long> {
    boolean existsByName(String name);
    List<GamesPostgres> findAllByIsActiveTrue(); // This is crazy that this shit works
}
