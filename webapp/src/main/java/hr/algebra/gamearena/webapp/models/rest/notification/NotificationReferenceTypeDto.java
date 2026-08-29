package hr.algebra.gamearena.webapp.models.rest.notification;

import hr.algebra.gamearena.webapp.models.cereal.notification.NotificationReferenceTypeDecereal;

import java.util.Optional;

public enum NotificationReferenceTypeDto {
    MATCH,
    TEAM_INVITATION;

    public static Optional<NotificationReferenceTypeDto> fromNotificationTypeDecereal(Optional<NotificationReferenceTypeDecereal> decereal) {
        return decereal.flatMap(notificationReferenceTypeDecereal -> switch (notificationReferenceTypeDecereal) {
            case MATCH -> Optional.of(MATCH);
            case TEAM_INVITATION -> Optional.of(TEAM_INVITATION);
        });

    }
}
