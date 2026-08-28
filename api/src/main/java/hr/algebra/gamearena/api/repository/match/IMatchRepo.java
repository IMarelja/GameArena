package hr.algebra.gamearena.api.repository.match;

import hr.algebra.gamearena.api.model.match.Match;
import hr.algebra.gamearena.api.model.match.MatchQuery;
import hr.algebra.gamearena.api.model.match.MatchSave;
import hr.algebra.gamearena.api.model.match.MatchUpdate;

import java.util.List;
import java.util.Optional;

public interface IMatchRepo {
    Optional<Match> getById(Long id);
    Match create(MatchSave matchSave);
    Optional<Match> update(Long id, MatchUpdate matchUpdate);
    List<Match> getAllByTournamentId(Long tournamentId);
    List<Match> getAllByPlayerId(Long userId);
    List<Match> getAllByQuery(MatchQuery query);
}
