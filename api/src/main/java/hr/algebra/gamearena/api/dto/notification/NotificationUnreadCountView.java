package hr.algebra.gamearena.api.dto.notification;

public record NotificationUnreadCountView(
        Integer count
) {
    public static NotificationUnreadCountView fromNotification(Integer count) {
        return new NotificationUnreadCountView(count);
    }
}
