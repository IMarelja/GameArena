package hr.algebra.gamearena.api.service.match;

import hr.algebra.gamearena.api.dto.match.MatchCreateRequest;
import hr.algebra.gamearena.api.dto.match.MatchFullView;

import java.util.Optional;

public interface IMatchService {
    Optional<MatchFullView> getById(Long id);
    MatchFullView createMatchAndPushNotification(Long callerId, MatchCreateRequest request);
}
