package hr.algebra.gamearena.webapp.service.match;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchFullViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;

import java.util.List;

public interface IMatchService {
    ApiResult<List<MatchFullViewDecereal>> getMyMatches() throws UnauthorizedException, NotFoundException;
}
