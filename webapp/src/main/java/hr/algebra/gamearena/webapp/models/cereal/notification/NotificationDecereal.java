package hr.algebra.gamearena.webapp.models.cereal.notification;

import java.time.OffsetDateTime;
import java.util.Optional;

public record NotificationDecereal(
        Long id,
        NotificationTypeDecereal type,
        Optional<Long> referenceId,
        Optional<NotificationReferenceTypeDecereal> referenceType,
        Boolean read,
        OffsetDateTime createdAt
) {
    public static NotificationDecereal fromNotificationMinimalViewClient(com.gamearena.client.model.NotificationMinimalView notification) {
        return new NotificationDecereal(
                notification.getId(),
                NotificationTypeDecereal.fromNotificationTypeClient(notification.getType()),
                notification.getReferenceId() == null ? Optional.empty() : Optional.of(notification.getReferenceId()),
                NotificationReferenceTypeDecereal.fromReferenceTypeClient(notification.getReferenceType()),
                notification.getRead(),
                notification.getCreatedAt()
        );
    }

    public static NotificationDecereal fromNotificationMinimalViewStreamClient(com.gamearena.streamclient.model.NotificationMinimalView notification) {
        return new NotificationDecereal(
                notification.getId(),
                NotificationTypeDecereal.fromNotificationTypeStreamClient(notification.getType()),
                notification.getReferenceId() == null ? Optional.empty() : Optional.of(notification.getReferenceId()),
                NotificationReferenceTypeDecereal.fromReferenceTypeSteamClient(notification.getReferenceType()),
                notification.getRead(),
                notification.getCreatedAt()
        );
    }
}
