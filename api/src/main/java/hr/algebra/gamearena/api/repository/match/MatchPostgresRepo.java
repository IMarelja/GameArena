package hr.algebra.gamearena.api.repository.match;

import hr.algebra.gamearena.api.model.match.Match;
import hr.algebra.gamearena.api.model.match.MatchSave;
import hr.algebra.gamearena.api.model.match.MatchUpdate;
import hr.algebra.gamearena.api.orm.postgres.match.MatchPostgres;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MatchPostgresRepo implements IMatchRepo {

    private final IMatchPostgreSQLRepo matchPostgreSQLRepo;

    public MatchPostgresRepo(IMatchPostgreSQLRepo matchPostgreSQLRepo) {
        this.matchPostgreSQLRepo = matchPostgreSQLRepo;
    }

    @Override
    public Optional<Match> getById(Long id) {
        return matchPostgreSQLRepo.findById(id)
                .map(Match::fromMatchPostgres);
    }

    @Override
    public Match create(MatchSave matchSave) {
        var matchPostgres = new MatchPostgres().fromMatchSave(matchSave);
        var saved = matchPostgreSQLRepo.save(matchPostgres);
        return Match.fromMatchPostgres(saved);
    }

    @Override
    public Optional<Match> update(Long id, MatchUpdate matchUpdate) {
        return matchPostgreSQLRepo.findById(id)
                .map(existing -> existing.fromMatchUpdate(matchUpdate))
                .map(matchPostgreSQLRepo::save)
                .map(Match::fromMatchPostgres);
    }

    @Override
    public List<Match> getAllByTournamentId(Long tournamentId) {
        return matchPostgreSQLRepo.findByTournamentId(tournamentId)
                .stream()
                .map(Match::fromMatchPostgres)
                .toList();
    }

    @Override
    public List<Match> getAllByPlayerId(Long userId) {
        return matchPostgreSQLRepo.findByPlayerOneIdOrPlayerTwoId(userId, userId)
                .stream()
                .map(Match::fromMatchPostgres)
                .toList();
    }
}
