package hr.algebra.gamearena.api.service.game;

import hr.algebra.gamearena.api.dto.games.GamesCreateRequst;
import hr.algebra.gamearena.api.exceptions.extenders.ConflictException;
import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.model.games.GamesSave;
import hr.algebra.gamearena.api.repository.games.IGamesRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GameService implements IGameService {

    private final IGamesRepo gamesRepo;

    public GameService(IGamesRepo gamesRepo) {
        this.gamesRepo = gamesRepo;
    }

    @Override
    public List<Games> getAll() {
        return gamesRepo.getAll();
    }

    @Override
    public Optional<Games> getById(Long id) {
        return gamesRepo.getById(id);
    }

    @Override
    public Games create(GamesCreateRequst games) {

        if (gamesRepo.existsByName(games.getName()))
            throw new ConflictException("A game with the name '" + games.getName() + "' already exists");

        var gamesSave = new GamesSave();
        gamesSave.setName(games.getName());
        gamesSave.setDescription(games.getDescription());

        return gamesRepo.save(gamesSave);
    }
}
