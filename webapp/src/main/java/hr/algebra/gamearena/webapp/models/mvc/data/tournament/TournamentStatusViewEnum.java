package hr.algebra.gamearena.webapp.models.mvc.data.tournament;

import com.gamearena.client.model.TournamentEditRequest;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentStatusDecereal;

public enum TournamentStatusViewEnum {
    SCHEDULED,
    LIVE,
    ENDED,
    CANCELED,
    STATUS_ERROR;

    public static TournamentStatusViewEnum fromDecereal(TournamentStatusDecereal status) {
        return switch (status) {
            case SCHEDULED -> SCHEDULED;
            case LIVE -> LIVE;
            case ENDED -> ENDED;
            case CANCELED -> CANCELED;
            case STATUS_ERROR -> STATUS_ERROR;
        };
    }

    public TournamentEditRequest.StatusEnum toClient() {
        return switch (this) {
            case SCHEDULED -> TournamentEditRequest.StatusEnum.SCHEDULED;
            case LIVE -> TournamentEditRequest.StatusEnum.LIVE;
            case ENDED -> TournamentEditRequest.StatusEnum.ENDED;
            case CANCELED -> TournamentEditRequest.StatusEnum.CANCELED;
            case STATUS_ERROR -> throw new IllegalStateException(
                    "STATUS_ERROR is a display-only fallback and must be changed to a real status before saving");
        };
    }

    public String badgeClass() {
        return switch (this) {
            case SCHEDULED -> "text-bg-secondary";
            case LIVE -> "text-bg-success";
            case ENDED -> "text-bg-primary";
            case CANCELED -> "text-bg-light";
            case STATUS_ERROR -> "text-bg-danger";
        };
    }
}
