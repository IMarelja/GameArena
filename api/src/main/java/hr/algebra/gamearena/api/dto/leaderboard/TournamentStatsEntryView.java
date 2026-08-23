package hr.algebra.gamearena.api.dto.leaderboard;

import hr.algebra.gamearena.api.dto.tournament.TournamentJustNameView;
import hr.algebra.gamearena.api.model.tournament.Tournament;

public record TournamentStatsEntryView(
        TournamentJustNameView tournament,
        StatsView stats
) {
    public static TournamentStatsEntryView fromTournamentAndStatsView(Tournament tournament, StatsView stats) {
        return new TournamentStatsEntryView(
                TournamentJustNameView.fromTournament(tournament),
                stats
        );
    }
}
