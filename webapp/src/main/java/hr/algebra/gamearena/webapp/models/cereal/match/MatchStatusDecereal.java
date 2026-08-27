package hr.algebra.gamearena.webapp.models.cereal.match;

import com.gamearena.client.model.MatchDetailedFullView;

public enum MatchStatusDecereal {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED;

    public static MatchStatusDecereal fromMatchStatusClient(MatchDetailedFullView.StatusEnum status) {
        return switch (status) {
            case SCHEDULED -> SCHEDULED;
            case IN_PROGRESS -> IN_PROGRESS;
            case COMPLETED -> COMPLETED;
            case CANCELED -> CANCELED;
        };
    }

    public String badgeClass() {
        return switch (this) {
            case SCHEDULED -> "text-bg-info";
            case IN_PROGRESS -> "text-bg-success";
            case COMPLETED -> "text-bg-secondary";
            case CANCELED -> "text-bg-danger";
        };
    }
}
