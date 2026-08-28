package hr.algebra.gamearena.webapp.models.mvc.data.leaderboard;

import hr.algebra.gamearena.webapp.models.cereal.leaderboard.TournamentStatsEntryViewDecereal;

public record TournamentStatsEntryViewData(
        Long tournamentId,
        String tournamentName,
        Integer matchesPlayed,
        Integer matchesWon,
        Integer totalPoints,
        Double averagePoints
) {
    public static TournamentStatsEntryViewData fromTournamentStatsEntryViewDecereal(TournamentStatsEntryViewDecereal statsEntry) {
        return new TournamentStatsEntryViewData(
                statsEntry.tournamentId(),
                statsEntry.tournamentName(),
                statsEntry.matchesPlayed(),
                statsEntry.matchesWon(),
                statsEntry.totalPoints(),
                statsEntry.averagePoints()
        );
    }
}
