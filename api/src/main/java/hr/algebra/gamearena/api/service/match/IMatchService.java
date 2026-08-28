package hr.algebra.gamearena.api.service.match;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.match.MatchCreateRequest;
import hr.algebra.gamearena.api.dto.match.MatchDetailedFullView;
import hr.algebra.gamearena.api.dto.match.MatchEditRequest;
import hr.algebra.gamearena.api.dto.match.MatchQueryDto;

import java.util.List;
import java.util.Optional;

public interface IMatchService {
    Optional<MatchDetailedFullView> getById(Long id);
    List<MatchDetailedFullView> getMatchesByUserId(Long userId);
    List<MatchDetailedFullView> getMatchesTournamentId(Long tournamentId);
    List<MatchDetailedFullView> queryMatches(MatchQueryDto query);
    MatchDetailedFullView createMatchAndPushNotification(JwtTokenClaim caller, MatchCreateRequest request);
    MatchDetailedFullView editMatch(JwtTokenClaim caller, Long id, MatchEditRequest request);
}
