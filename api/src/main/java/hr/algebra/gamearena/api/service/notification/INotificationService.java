package hr.algebra.gamearena.api.service.notification;

import hr.algebra.gamearena.api.dto.notification.NotificationCountView;
import hr.algebra.gamearena.api.dto.notification.NotificationCreateRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationEditRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationMinimalView;

import java.util.List;
import java.util.Optional;

public interface INotificationService {
    Optional<NotificationMinimalView> findById(Long id);
    List<NotificationMinimalView> findAllByUserIdUnreadByDescendingDate(Long userId);
    List<NotificationMinimalView> findAllByUserIdByDescendingDate(Long userId);
    NotificationCountView countUnreadByUserId(Long userId);
    NotificationMinimalView create(NotificationCreateRequest request);
    Optional<NotificationMinimalView> update(Long id, NotificationEditRequest request);
    boolean deleteById(Long id);
    boolean doesUserOwnNotification(Long userId, Long notificationId);

}
