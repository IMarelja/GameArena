package hr.algebra.gamearena.api.repository.games;

import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.model.games.GamesSave;

import java.util.List;
import java.util.Optional;

public interface IGamesRepo {
    List<Games> getAll();
    List<Games> getByIsActiveTrue();
    Optional<Games> getById(Long id);
    boolean existsByName(String name);
    Games save(GamesSave gamesSave);
}
