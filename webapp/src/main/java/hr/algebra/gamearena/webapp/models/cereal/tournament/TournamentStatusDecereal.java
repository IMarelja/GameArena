package hr.algebra.gamearena.webapp.models.cereal.tournament;

import com.gamearena.client.model.TournamentFullView;

public enum TournamentStatusDecereal {
    SCHEDULED,
    LIVE,
    ENDED,
    CANCELED;

    public static TournamentStatusDecereal fromTournamentStatusClient(TournamentFullView.StatusEnum status) {
        return switch (status) {
            case SCHEDULED -> SCHEDULED;
            case LIVE -> LIVE;
            case ENDED -> ENDED;
            case CANCELED -> CANCELED;
        };
    }

    /**
     * Bootstrap background color class for this status - the color scheme the tournament list
     * and detail pages badge the status with.
     */
    public String badgeClass() {
        return switch (this) {
            case SCHEDULED -> "text-bg-info";
            case LIVE -> "text-bg-success";
            case ENDED -> "text-bg-secondary";
            case CANCELED -> "text-bg-danger";
        };
    }
}
