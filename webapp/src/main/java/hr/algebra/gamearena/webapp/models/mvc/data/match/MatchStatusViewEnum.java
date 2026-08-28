package hr.algebra.gamearena.webapp.models.mvc.data.match;

import com.gamearena.client.model.MatchEditRequest;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchStatusDecereal;

public enum MatchStatusViewEnum {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED;

    public static MatchStatusViewEnum fromDecereal(MatchStatusDecereal status) {
        return switch (status) {
            case SCHEDULED -> SCHEDULED;
            case IN_PROGRESS -> IN_PROGRESS;
            case COMPLETED -> COMPLETED;
            case CANCELED -> CANCELED;
        };
    }

    public MatchEditRequest.StatusEnum toClient() {
        return switch (this) {
            case SCHEDULED -> MatchEditRequest.StatusEnum.SCHEDULED;
            case IN_PROGRESS -> MatchEditRequest.StatusEnum.IN_PROGRESS;
            case COMPLETED -> MatchEditRequest.StatusEnum.COMPLETED;
            case CANCELED -> MatchEditRequest.StatusEnum.CANCELED;
        };
    }

    public String badgeClass() {
        return switch (this) {
            case SCHEDULED -> "text-bg-secondary";
            case IN_PROGRESS -> "text-bg-success";
            case COMPLETED -> "text-bg-primary";
            case CANCELED -> "text-bg-danger";
        };
    }
}
