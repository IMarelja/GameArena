package hr.algebra.gamearena.api.dto.notification;

import hr.algebra.gamearena.api.model.notification.Notification;

public record NotificationCountView(
        Integer count
) {
    public static NotificationCountView fromNotification(Integer count) {
        return new NotificationCountView(count);
    }
}
