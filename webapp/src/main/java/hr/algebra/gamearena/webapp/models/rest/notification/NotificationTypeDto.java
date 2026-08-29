package hr.algebra.gamearena.webapp.models.rest.notification;

import hr.algebra.gamearena.webapp.models.cereal.notification.NotificationTypeDecereal;

public enum NotificationTypeDto {
    CREATED_MATCH,
    TEAM_INVITATION,
    TEAM_INVITATION_RESPONSE,
    TEAM_INVITATION_UPDATE,
    TEST,
    ERROR;

    public static NotificationTypeDto fromNotificationTypeDecereal(NotificationTypeDecereal decereal) {
        if(decereal == null) {
            return ERROR;
        }

        return switch (decereal) {
            case CREATED_MATCH -> CREATED_MATCH;
            case TEAM_INVITATION -> TEAM_INVITATION;
            case TEAM_INVITATION_RESPONSE -> TEAM_INVITATION_RESPONSE;
            case TEAM_INVITATION_UPDATE -> TEAM_INVITATION_UPDATE;
            case TEST -> TEST;
            case ERROR -> ERROR;
        };
    }
}
