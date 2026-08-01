package hr.algebra.gamearena.api.service.game;

import hr.algebra.gamearena.api.dto.games.GamesCreateRequest;
import hr.algebra.gamearena.api.dto.games.GamesView;

import java.util.List;
import java.util.Optional;

public interface IGameService {
    List<GamesView> getAll();
    List<GamesView> getAllActive();
    Optional<GamesView> getById(Long id);
    GamesView create(GamesCreateRequest game);
}
