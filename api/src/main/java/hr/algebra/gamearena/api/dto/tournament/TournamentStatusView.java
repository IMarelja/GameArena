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
}
