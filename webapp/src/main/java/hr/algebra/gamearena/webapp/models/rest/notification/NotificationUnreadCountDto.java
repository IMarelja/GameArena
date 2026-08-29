package hr.algebra.gamearena.webapp.models.rest.notification;

import hr.algebra.gamearena.webapp.models.cereal.notification.NotificationUnreadCountDecereal;

public record NotificationUnreadCountDto(
        Integer count
) {
    public static NotificationUnreadCountDto fromNotificationUnreadCountDecereal(NotificationUnreadCountDecereal decereal) {
        return new NotificationUnreadCountDto(
                decereal.count()
        );
    }
}
