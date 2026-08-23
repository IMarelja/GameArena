package hr.algebra.gamearena.api.dto.notification;

import hr.algebra.gamearena.api.model.notification.NotificationType;

public enum NotificationTypeView {
    CREATED_MATCH,
    TEAM_INVITATION,
    TEAM_INVITATION_RESPONSE,
    TEAM_INVITATION_UPDATE,
    TEST;

    public static NotificationTypeView fromNotificationType(NotificationType type) {
        return switch (type) {
            case CREATED_MATCH -> CREATED_MATCH;
            case TEAM_INVITATION -> TEAM_INVITATION;
            case TEAM_INVITATION_RESPONSE -> TEAM_INVITATION_RESPONSE;
            case TEAM_INVITATION_UPDATE -> TEAM_INVITATION_UPDATE;
            case TEST -> TEST;
        };
    }

    public NotificationType toNotificationType() {
        return switch (this) {
            case CREATED_MATCH -> NotificationType.CREATED_MATCH;
            case TEAM_INVITATION -> NotificationType.TEAM_INVITATION;
            case TEAM_INVITATION_RESPONSE -> NotificationType.TEAM_INVITATION_RESPONSE;
            case TEAM_INVITATION_UPDATE -> NotificationType.TEAM_INVITATION_UPDATE;
            case TEST -> NotificationType.TEST;
        };
    }
}
