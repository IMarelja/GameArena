package hr.algebra.gamearena.api.repository.games;

import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.model.games.GamesSave;
import hr.algebra.gamearena.api.model.games.GamesUpdate;

import java.util.List;
import java.util.Optional;

public interface IGamesRepo {
    List<Games> getAll();
    List<Games> getByIsActiveTrue();
    Optional<Games> getById(Long id);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    Games save(GamesSave gamesSave);
    Optional<Games> update(Long id, GamesUpdate gamesUpdate);
}
