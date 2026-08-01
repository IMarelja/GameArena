package hr.algebra.gamearena.api.repository.games;

import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.model.games.GamesSave;
import hr.algebra.gamearena.api.orm.postgres.GamesPostgres;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GamesPostgresRepo implements IGamesRepo{

    private final IGamesPostgreSQLRepo sqlGamesPostgresRepo;

    public GamesPostgresRepo(IGamesPostgreSQLRepo sqlGamesPostgresRepo) {
        this.sqlGamesPostgresRepo = sqlGamesPostgresRepo;
    }

    @Override
    public List<Games> getAll() {
        return sqlGamesPostgresRepo.findAll()
                .stream()
                .map(Games::fromPostgres)
                .toList();
    }

    @Override
    public List<Games> getByIsActiveTrue() {
        return sqlGamesPostgresRepo.findAllByIsActiveTrue()
                .stream()
                .map(Games::fromPostgres)
                .toList();
    }

    @Override
    public Optional<Games> getById(Long id) {
        return sqlGamesPostgresRepo.findById(id)
                .map(Games::fromPostgres);
    }

    @Override
    public boolean existsByName(String name) {
        return sqlGamesPostgresRepo.existsByName(name);
    }

    @Override
    public Games save(GamesSave gamesSave) {
        var userPostgres = new GamesPostgres().fromGamesCreate(gamesSave);
        var savedGames = sqlGamesPostgresRepo.save(userPostgres);
        return Games.fromPostgres(savedGames);
    }
}
