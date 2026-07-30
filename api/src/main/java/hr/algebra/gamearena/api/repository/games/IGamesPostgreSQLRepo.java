package hr.algebra.gamearena.api.repository.games;

import hr.algebra.gamearena.api.orm.postgres.GamesPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGamesPostgreSQLRepo extends JpaRepository<GamesPostgres, Long> {
    boolean existsByName(String name);
}
