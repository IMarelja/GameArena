package hr.algebra.gamearena.webapp.models.cereal.notification;

import com.gamearena.streamclient.model.NotificationMinimalView;

import java.time.OffsetDateTime;

public record NotificationDecereal(
        Long id,
        NotificationTypeDecereal type,
        Boolean read,
        OffsetDateTime createdAt
) {
    public static NotificationDecereal fromNotificationMinimalViewClient(NotificationMinimalView notification) {
        return new NotificationDecereal(
                notification.getId(),
                NotificationTypeDecereal.fromNotificationTypeClient(notification.getType()),
                notification.getRead(),
                notification.getCreatedAt()
        );
    }
}
