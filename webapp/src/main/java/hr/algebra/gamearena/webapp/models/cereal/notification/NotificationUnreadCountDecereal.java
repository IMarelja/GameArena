package hr.algebra.gamearena.webapp.models.cereal.notification;

import com.gamearena.streamclient.model.NotificationUnreadCountView;

public record NotificationUnreadCountDecereal(
        Integer count
) {
    public static NotificationUnreadCountDecereal fromNotificationUnreadCountViewClient(NotificationUnreadCountView view) {
        return new NotificationUnreadCountDecereal(view.getCount());
    }
}
