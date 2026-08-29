package hr.algebra.gamearena.webapp.models.cereal.match;

import com.gamearena.client.model.MatchDetailedFullView;

public enum MatchStatusDecereal {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED,
    ERROR;

    public static MatchStatusDecereal fromMatchStatusClient(MatchDetailedFullView.StatusEnum status) {
        if(status == null){
            return ERROR;
        }

        return switch (status) {
            case SCHEDULED -> SCHEDULED;
            case IN_PROGRESS -> IN_PROGRESS;
            case COMPLETED -> COMPLETED;
            case CANCELED -> CANCELED;
        };
    }
}
