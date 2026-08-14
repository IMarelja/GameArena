package hr.algebra.gamearena.api.model.notification;

import hr.algebra.gamearena.api.orm.postgres.NotificationPostgres;

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
    public static Notification fromPostgresORM(NotificationPostgres notification) {
        return new Notification(
                notification.getId(),
                notification.getType(),
                notification.getRecipientUserId(),
                notification.getReferenceId(),
                notification.getReferenceType(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
