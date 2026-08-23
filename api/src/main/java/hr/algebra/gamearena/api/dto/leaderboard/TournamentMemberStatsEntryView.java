package hr.algebra.gamearena.api.dto.leaderboard;

import hr.algebra.gamearena.api.dto.user.UserJustUsernameView;
import hr.algebra.gamearena.api.model.user.User;

public record TournamentMemberStatsEntryView(
        UserJustUsernameView user,
        StatsView stats
) {
    public static TournamentMemberStatsEntryView fromUserAndStatsView(User user, StatsView stats) {
        return new TournamentMemberStatsEntryView(
                UserJustUsernameView.fromUser(user),
                stats
        );
    }
}
