package hr.algebra.gamearena.webapp.service.leaderboard;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.cereal.leaderboard.TournamentStatsEntryViewDecereal;

import java.util.List;

public interface ILeaderboardService {
    List<TournamentStatsEntryViewDecereal> getMyStats() throws UnauthorizedException, NotFoundException, UnexpectedApiErrorException;
    List<TournamentStatsEntryViewDecereal> getStatsByUserId(Long id) throws NotFoundException, UnexpectedApiErrorException;
}
