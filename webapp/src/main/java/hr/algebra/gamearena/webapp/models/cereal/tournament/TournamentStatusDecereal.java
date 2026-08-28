package hr.algebra.gamearena.webapp.models.cereal.tournament;

import com.gamearena.client.model.TournamentFullView;

public enum TournamentStatusDecereal {
    SCHEDULED,
    LIVE,
    ENDED,
    CANCELED,
    STATUS_ERROR;

    public static TournamentStatusDecereal fromTournamentStatusClient(TournamentFullView.StatusEnum status) {
        if (status == null) {
            return STATUS_ERROR;
        }

        return switch (status) {
            case SCHEDULED -> SCHEDULED;
            case LIVE -> LIVE;
            case ENDED -> ENDED;
            case CANCELED -> CANCELED;
        };
    }
}
