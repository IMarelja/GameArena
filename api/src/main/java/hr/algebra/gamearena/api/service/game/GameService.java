package hr.algebra.gamearena.api.service.game;

import hr.algebra.gamearena.api.dto.games.GamesCreateRequest;
import hr.algebra.gamearena.api.dto.games.GamesView;
import hr.algebra.gamearena.api.exceptions.extenders.ConflictException;
import hr.algebra.gamearena.api.model.games.GamesSave;
import hr.algebra.gamearena.api.repository.games.IGamesRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static hr.algebra.gamearena.api.dto.games.GamesView.toGamesView;

@Slf4j
@Service
public class GameService implements IGameService {

    private final IGamesRepo gamesRepo;

    public GameService(IGamesRepo gamesRepo) {
        this.gamesRepo = gamesRepo;
    }

    @Override
    public List<GamesView> getAll() {
        return gamesRepo.getAll()
                .stream()
                .map(GamesView::toGamesView)
                .toList();
    }

    @Override
    public Optional<GamesView> getById(Long id) {
        return gamesRepo.getById(id).map(GamesView::toGamesView);
    }

    @Override
    public GamesView create(GamesCreateRequest games) {

        if (gamesRepo.existsByName(games.getName()))
            throw new ConflictException("A game with the name '" + games.getName() + "' already exists");

        var gamesSave = new GamesSave();
        gamesSave.setName(games.getName());
        gamesSave.setDescription(games.getDescription());

        return toGamesView (gamesRepo.save(gamesSave));
    }
}
