package hr.algebra.gamearena.api.service.leaderboard;

import hr.algebra.gamearena.api.dto.leaderboard.StatsView;
import hr.algebra.gamearena.api.dto.leaderboard.TournamentMemberStatsEntryView;
import hr.algebra.gamearena.api.dto.leaderboard.TournamentStatsEntryView;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.model.match.Match;
import hr.algebra.gamearena.api.model.match.MatchStatus;
import hr.algebra.gamearena.api.repository.match.IMatchRepo;
import hr.algebra.gamearena.api.repository.tournament.ITournamentRepo;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LeaderboardService implements ILeaderboardService {

    private final IMatchRepo matchRepo;
    private final ITournamentRepo tournamentRepo;
    private final IUserRepo userRepo;

    public LeaderboardService(IMatchRepo matchRepo, ITournamentRepo tournamentRepo, IUserRepo userRepo) {
        this.matchRepo = matchRepo;
        this.tournamentRepo = tournamentRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<TournamentMemberStatsEntryView> getTournamentLeaderboard(Long tournamentId) {
        if (!tournamentRepo.tournamentExistsById(tournamentId)) {
            throw new NotFoundException("Tournament not found with id: " + tournamentId);
        }

        var completedMatches = matchRepo.getAllByTournamentId(tournamentId)
                            .stream()
                            .filter(match -> match.status() == MatchStatus.COMPLETED)
                            .toList();

        var entries = new ArrayList<TournamentMemberStatsEntryView>();
        for (var member : tournamentRepo.getAllTournamentMembersFromTournamentId(tournamentId)) {
            var stats = computeStats(member.userId(), completedMatches);
            var user = userRepo.findById(member.userId()).orElse(null);

            entries.add(user != null
                    ? TournamentMemberStatsEntryView.fromUserAndStatsView(user, stats)
                    : new TournamentMemberStatsEntryView(null, stats));
        }

        entries.sort(Comparator.<TournamentMemberStatsEntryView>comparingInt(entry -> entry.stats().matchesWon()).reversed());

        return entries;
    }

    @Override
    public List<TournamentStatsEntryView> getStatsFromUserId(Long userId) {
        var matchesByTournament = matchRepo.getAllByPlayerId(userId)
                            .stream()
                            .filter(match -> match.status() == MatchStatus.COMPLETED)
                            .filter(match -> match.tournamentId() != null)
                            .collect(Collectors.groupingBy(Match::tournamentId));

        var entries = new ArrayList<TournamentStatsEntryView>();
        for (var tournamentMatches : matchesByTournament.entrySet()) {
            var stats = computeStats(userId, tournamentMatches.getValue());
            var tournament = tournamentRepo.getTournamentById(tournamentMatches.getKey()).orElse(null);

            entries.add(tournament != null
                    ? TournamentStatsEntryView.fromTournamentAndStatsView(tournament, stats)
                    : new TournamentStatsEntryView(null, stats));
        }

        return entries;
    }

    private StatsView computeStats(Long userId, List<Match> matches) {
        int matchesPlayed = 0;
        int matchesWon = 0;
        int totalPoints = 0;

        for (var match : matches) {
            boolean isPlayerOne = userId.equals(match.playerOneId());
            boolean isPlayerTwo = userId.equals(match.playerTwoId());

            if (!isPlayerOne && !isPlayerTwo) {
                continue;
            }

            matchesPlayed++;

            if (userId.equals(match.winnerId())) {
                matchesWon++;
            }

            Integer score = isPlayerOne ? match.playerOneScore() : match.playerTwoScore();
            totalPoints += score != null ? score : 0;
        }

        double averagePoints = matchesPlayed > 0 ? (double) totalPoints / matchesPlayed : 0.0;

        return new StatsView(matchesPlayed, matchesWon, totalPoints, averagePoints);
    }
}
