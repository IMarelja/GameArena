package hr.algebra.gamearena.api.dto.tournament;

import hr.algebra.gamearena.api.model.tournament.TournamentStatus;

public enum TournamentStatusView {
    SCHEDULED,
    LIVE,
    ENDED,
    CANCELED;

    public static TournamentStatusView fromTournamentStatus(TournamentStatus status){
        return switch (status) {
            case SCHEDULED -> TournamentStatusView.SCHEDULED;
            case LIVE -> TournamentStatusView.LIVE;
            case ENDED -> TournamentStatusView.ENDED;
            case CANCELED -> TournamentStatusView.CANCELED;
        };
    }

    public TournamentStatus toTournamentStatus() {
        return switch (this) {
            case SCHEDULED -> TournamentStatus.SCHEDULED;
            case LIVE -> TournamentStatus.LIVE;
            case ENDED -> TournamentStatus.ENDED;
            case CANCELED -> TournamentStatus.CANCELED;
        };
    }
}
