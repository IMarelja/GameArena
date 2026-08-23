package hr.algebra.gamearena.api.dto.notification;

import hr.algebra.gamearena.api.model.notification.Notification;

import java.time.LocalDateTime;

public record NotificationMinimalView(
    Long id,
    NotificationTypeView type,
    Long referenceId,
    ReferenceTypeView referenceType,
    Boolean read,
    LocalDateTime createdAt
) {
    public static NotificationMinimalView fromNotification(Notification notification) {
        return new NotificationMinimalView(
                notification.id(),
                NotificationTypeView.fromNotificationType(notification.notificationType()),
                notification.referenceId(),
                ReferenceTypeView.fromReferenceType(notification.referenceType()),
                notification.read(),
                notification.createdAt()
        );
    }
}
