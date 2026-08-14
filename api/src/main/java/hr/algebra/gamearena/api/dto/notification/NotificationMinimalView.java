package hr.algebra.gamearena.api.dto.notification;

import hr.algebra.gamearena.api.model.notification.Notification;
import hr.algebra.gamearena.api.model.notification.NotificationType;
import hr.algebra.gamearena.api.model.notification.ReferenceType;

import java.time.LocalDateTime;

public record NotificationMinimalView(
    Long id,
    NotificationType type,
    Long referenceId,
    ReferenceType referenceType,
    Boolean read,
    LocalDateTime createdAt
) {
    public static NotificationMinimalView fromNotification(Notification notification) {
        return new NotificationMinimalView(
                notification.id(),
                notification.notificationType(),
                notification.referenceId(),
                notification.referenceType(),
                notification.read(),
                notification.createdAt()
        );
    }
}
