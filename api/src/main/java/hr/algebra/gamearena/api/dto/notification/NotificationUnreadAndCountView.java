package hr.algebra.gamearena.api.dto.notification;

import java.util.List;

public record NotificationUnreadAndCountView(
        Integer count,
        List<NotificationMinimalView> unread
) {
}
