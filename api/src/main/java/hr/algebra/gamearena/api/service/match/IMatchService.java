package hr.algebra.gamearena.api.service.match;

import hr.algebra.gamearena.api.dto.match.MatchCreateRequest;
import hr.algebra.gamearena.api.dto.match.MatchFullView;

import java.util.List;
import java.util.Optional;

public interface IMatchService {
    Optional<MatchFullView> getById(Long id);
    List<MatchFullView> getMatchesByUserId(Long userId);
    MatchFullView createMatchAndPushNotification(Long callerId, MatchCreateRequest request);
}
