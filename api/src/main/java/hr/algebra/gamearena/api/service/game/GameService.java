package hr.algebra.gamearena.api.service.game;

import hr.algebra.gamearena.api.dto.games.GamesCreateRequest;
import hr.algebra.gamearena.api.dto.games.GamesEditRequest;
import hr.algebra.gamearena.api.dto.games.GamesFullView;
import hr.algebra.gamearena.api.dto.games.GamesView;
import hr.algebra.gamearena.api.exceptions.extenders.ConflictException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.model.games.GamesSave;
import hr.algebra.gamearena.api.model.games.GamesUpdate;
import hr.algebra.gamearena.api.repository.games.IGamesRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GameService implements IGameService {

    private static String GameNotFoundByIdOutput(Long id){
        return "No game found with the id '" + id + "'";
    }

    private static String GameFoundByNameConflictOutput(String name){
        return "A game with the name '" + name + "' already exists";
    }


    private final IGamesRepo gamesRepo;

    public GameService(IGamesRepo gamesRepo) {
        this.gamesRepo = gamesRepo;
    }

    @Override
    public List<GamesView> getAll() {
        return gamesRepo.getAll()
                .stream()
                .map(GamesView::fromGamesModel)
                .toList();
    }

    @Override
    public List<GamesView> getAllActive() {
        return gamesRepo.getByIsActiveTrue()
                .stream()
                .map(GamesView::fromGamesModel)
                .toList();
    }

    @Override
    public Optional<GamesView> getById(Long id) {
        return Optional.of(gamesRepo.getById(id)
                .map(GamesView::fromGamesModel)
                .orElseThrow(() -> new NotFoundException( GameNotFoundByIdOutput(id) )));
    }

    @Override
    public Optional<GamesFullView> getByIdFull(Long id) {
        return Optional.of(gamesRepo.getById(id)
                .map(GamesFullView::fromGamesModel)
                .orElseThrow(() -> new NotFoundException( GameNotFoundByIdOutput(id) )));
    }

    @Override
    public GamesFullView create(GamesCreateRequest games) {

        if (gamesRepo.existsByName(games.getName()))
            throw new ConflictException( GameFoundByNameConflictOutput(games.getName()) );

        var gamesSave = new GamesSave();
        gamesSave.setName(games.getName());
        gamesSave.setDescription(games.getDescription());

        return GamesFullView.fromGamesModel (gamesRepo.save(gamesSave));
    }

    @Override
    public GamesFullView update(Long id, GamesEditRequest gamesEditRequest) {
        if (gamesRepo.existsByNameAndIdNot(gamesEditRequest.getName(), id))
            throw new ConflictException( GameFoundByNameConflictOutput(gamesEditRequest.getName()) );

        var gamesUpdate = new GamesUpdate();
        gamesUpdate.setName(gamesEditRequest.getName());
        gamesUpdate.setDescription(gamesEditRequest.getDescription());
        gamesUpdate.setActive(gamesEditRequest.getIsActive());

        return gamesRepo.update(id, gamesUpdate)
                .map(GamesFullView::fromGamesModel)
                .orElseThrow(() -> new NotFoundException(GameNotFoundByIdOutput(id)));
    }
}
