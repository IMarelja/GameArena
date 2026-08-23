package hr.algebra.gamearena.api.dto.leaderboard;

public record StatsView(
        Integer matchesPlayed,
        Integer matchesWon,
        Integer totalPoints,
        Double averagePoints
) {
}
