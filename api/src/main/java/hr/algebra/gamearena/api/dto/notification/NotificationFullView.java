package hr.algebra.gamearena.api.dto.notification;

import hr.algebra.gamearena.api.model.notification.Notification;
import hr.algebra.gamearena.api.model.notification.NotificationType;
import hr.algebra.gamearena.api.model.notification.ReferenceType;

import java.time.LocalDateTime;

public record NotificationFullView(
    Long id,
    NotificationType type,
    Long recipientUserId,
    Long referenceId,
    ReferenceType referenceType,
    Boolean read,
    LocalDateTime createdAt
) {
    public static NotificationFullView fromNotification(Notification notification) {
        return new NotificationFullView(
            notification.id(),
            notification.notificationType(),
            notification.receiverUserId(),
            notification.referenceId(),
            notification.referenceType(),
            notification.read(),
            notification.createdAt()
        );
    }

    public NotificationMinimalView toMinimalView() {
        return new NotificationMinimalView(id, type, referenceId, referenceType, read, createdAt);
    }
}
