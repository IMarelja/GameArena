package hr.algebra.gamearena.api.dto.match;

import hr.algebra.gamearena.api.model.match.MatchStatus;

public enum MatchStatusView {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED;


    public static MatchStatusView fromMatchStatus(MatchStatus status) {
        return switch (status){
            case SCHEDULED -> SCHEDULED;
            case IN_PROGRESS -> IN_PROGRESS;
            case COMPLETED -> COMPLETED;
            case CANCELED -> CANCELED;
        };
    }
}
