package hr.algebra.gamearena.api.dto.notification;

public record NotificationCountView(
        Integer count
) {
    public static NotificationCountView fromNotification(Integer count) {
        return new NotificationCountView(count);
    }
}
