package hr.algebra.gamearena.api.service.leaderboard;

import hr.algebra.gamearena.api.dto.leaderboard.TournamentMemberStatsEntryView;
import hr.algebra.gamearena.api.dto.leaderboard.TournamentStatsEntryView;

import java.util.List;

public interface ILeaderboardService {
    List<TournamentMemberStatsEntryView> getTournamentLeaderboard(Long tournamentId);
    List<TournamentStatsEntryView> getMyStats(Long callerId);
}
