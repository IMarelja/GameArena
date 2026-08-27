package hr.algebra.gamearena.api.model.notification;

import hr.algebra.gamearena.api.orm.postgres.notification.NotificationPostgres;

import java.time.LocalDateTime;

public record Notification(
        Long id,
        NotificationType notificationType,
        Long receiverUserId,
        Long referenceId,
        ReferenceType referenceType,
        Boolean read,
        LocalDateTime createdAt
) {
    public static Notification fromNotificationPostgres(NotificationPostgres notification) {
        return new Notification(
                notification.getId(),
                NotificationType.valueOf(notification.getType()),
                notification.getRecipientUserId(),
                notification.getReferenceId(),
                notification.getReferenceType() != null ? ReferenceType.valueOf(notification.getReferenceType()) : null,
                notification.isRead(),
                notification.getCreatedAt().toLocalDateTime()
        );
    }
}
