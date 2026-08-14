package hr.algebra.gamearena.api.repository.notification;

import hr.algebra.gamearena.api.model.notification.Notification;
import hr.algebra.gamearena.api.model.notification.NotificationSave;
import hr.algebra.gamearena.api.model.notification.NotificationUpdate;

import java.util.List;
import java.util.Optional;

public interface INotificationRepo {
    List<Notification> findAll();
    Optional<Notification> findById(Long id);
    List<Notification> findUnreadByUserIdByDescendingDate(Long id);
    List<Notification> findAllByUserIdByDescendingDate(Long id);
    Integer countUnreadByUserIdByDescendingDate(Long id);
    boolean doesUserOwnThisNotification(Long userId, Long notificationId);
    Notification save(NotificationSave save);
    Notification update(Long id, NotificationUpdate update);
    void delete(Long id);
}
