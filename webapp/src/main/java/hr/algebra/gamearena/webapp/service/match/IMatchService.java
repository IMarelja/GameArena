package hr.algebra.gamearena.webapp.service.match;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchDetailFullViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;

import java.util.List;

public interface IMatchService {
    ApiResult<List<MatchDetailFullViewDecereal>> getMyMatches() throws UnauthorizedException, NotFoundException;
    ApiResult<List<MatchDetailFullViewDecereal>> getMatchesByUserId(Long id) throws NotFoundException;
}
