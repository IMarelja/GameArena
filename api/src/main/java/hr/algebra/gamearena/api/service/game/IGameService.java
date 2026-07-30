package hr.algebra.gamearena.api.service.game;

import hr.algebra.gamearena.api.dto.games.GamesCreate;
import hr.algebra.gamearena.api.model.games.Games;

import java.util.List;
import java.util.Optional;

public interface IGameService {
    List<Games> getAll();
    Optional<Games> getById(Long id);
    Games create(GamesCreate game);
}
