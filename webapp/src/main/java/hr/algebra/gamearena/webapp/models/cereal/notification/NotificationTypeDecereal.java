package hr.algebra.gamearena.webapp.models.cereal.notification;

import com.gamearena.streamclient.model.NotificationMinimalView;

public enum NotificationTypeDecereal {
    CREATED_MATCH,
    TEAM_INVITATION,
    TEAM_INVITATION_RESPONSE,
    TEAM_INVITATION_UPDATE,
    TEST;

    public static NotificationTypeDecereal fromNotificationTypeClient(NotificationMinimalView.TypeEnum type) {
        return switch (type) {
            case CREATED_MATCH -> CREATED_MATCH;
            case TEAM_INVITATION -> TEAM_INVITATION;
            case TEAM_INVITATION_RESPONSE -> TEAM_INVITATION_RESPONSE;
            case TEAM_INVITATION_UPDATE -> TEAM_INVITATION_UPDATE;
            case TEST -> TEST;
        };
    }
}
