package hr.algebra.gamearena.webapp.models.cereal.leaderboard;

import com.gamearena.client.model.TournamentStatsEntryView;

public record TournamentStatsEntryViewDecereal(
        Long tournamentId,
        String tournamentName,
        Integer matchesPlayed,
        Integer matchesWon,
        Integer totalPoints,
        Double averagePoints
) {
    public static TournamentStatsEntryViewDecereal fromTournamentStatsEntryViewClient(TournamentStatsEntryView entry) {
        var tournament = entry.getTournament();
        var stats = entry.getStats();

        return new TournamentStatsEntryViewDecereal(
                tournament != null ? tournament.getId() : null,
                tournament != null ? tournament.getName() : null,
                stats != null ? stats.getMatchesPlayed() : null,
                stats != null ? stats.getMatchesWon() : null,
                stats != null ? stats.getTotalPoints() : null,
                stats != null ? stats.getAveragePoints() : null
        );
    }
}
