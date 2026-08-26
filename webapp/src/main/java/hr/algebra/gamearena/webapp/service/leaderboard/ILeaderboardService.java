package hr.algebra.gamearena.webapp.service.leaderboard;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.leaderboard.TournamentStatsEntryViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;

import java.util.List;

public interface ILeaderboardService {
    ApiResult<List<TournamentStatsEntryViewDecereal>> getMyStats() throws UnauthorizedException, NotFoundException;
    ApiResult<List<TournamentStatsEntryViewDecereal>> getStatsByUserId(Long id) throws NotFoundException;
}
