package hr.algebra.gamearena.api.repository.match;

import hr.algebra.gamearena.api.model.match.Match;
import hr.algebra.gamearena.api.model.match.MatchSave;

import java.util.Optional;

public interface IMatchRepo {
    Optional<Match> getById(Long id);
    Match create(MatchSave matchSave);
}
