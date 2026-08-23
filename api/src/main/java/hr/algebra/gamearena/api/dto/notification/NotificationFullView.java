package hr.algebra.gamearena.api.dto.notification;

import hr.algebra.gamearena.api.model.notification.Notification;

import java.time.LocalDateTime;

public record NotificationFullView(
    Long id,
    NotificationTypeView type,
    Long recipientUserId,
    Long referenceId,
    ReferenceTypeView referenceType,
    Boolean read,
    LocalDateTime createdAt
) {
    public static NotificationFullView fromNotification(Notification notification) {
        return new NotificationFullView(
            notification.id(),
            NotificationTypeView.fromNotificationType(notification.notificationType()),
            notification.receiverUserId(),
            notification.referenceId(),
            ReferenceTypeView.fromReferenceType(notification.referenceType()),
            notification.read(),
            notification.createdAt()
        );
    }

    public NotificationMinimalView toMinimalView() {
        return new NotificationMinimalView(id, type, referenceId, referenceType, read, createdAt);
    }
}
