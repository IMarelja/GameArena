package hr.algebra.gamearena.api.service.match;

import hr.algebra.gamearena.api.dto.match.MatchCreateRequest;
import hr.algebra.gamearena.api.dto.match.MatchDetailedFullView;

import java.util.List;
import java.util.Optional;

public interface IMatchService {
    Optional<MatchDetailedFullView> getById(Long id);
    List<MatchDetailedFullView> getMatchesByUserId(Long userId);
    List<MatchDetailedFullView> getMatchesTournamentId(Long tournamentId);
    MatchDetailedFullView createMatchAndPushNotification(Long callerId, MatchCreateRequest request);
}
