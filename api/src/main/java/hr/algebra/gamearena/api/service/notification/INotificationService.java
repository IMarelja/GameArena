package hr.algebra.gamearena.api.service.notification;

import hr.algebra.gamearena.api.dto.notification.NotificationUnreadCountView;
import hr.algebra.gamearena.api.dto.notification.NotificationCreateRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationEditRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationFullView;
import hr.algebra.gamearena.api.dto.notification.NotificationMinimalView;
import hr.algebra.gamearena.api.dto.notification.NotificationUnreadAndCountView;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface INotificationService {
    Optional<NotificationMinimalView> findById(Long id);
    List<NotificationMinimalView> findAllByUserIdUnreadByDescendingDate(Long userId);
    List<NotificationMinimalView> findAllByUserIdByDescendingDate(Long userId);
    NotificationUnreadCountView countUnreadByUserId(Long userId);
    NotificationMinimalView createAndPush(NotificationCreateRequest request);
    Optional<NotificationMinimalView> updateAndPush(Long id, NotificationEditRequest request);
    void deleteByIdAndPush(Long id);
    boolean doesUserOwnNotification(Long userId, Long notificationId);

    /**
     * The caller's unread-notifications snapshot right now (covers anything missed while
     * disconnected), followed by a fresh snapshot every time a new notification arrives for them.
     */
    Flux<NotificationUnreadAndCountView> streamForUser_unreadAndCount(Long userId);
    Flux<NotificationUnreadCountView> streamToUser_unreadCount(Long userId);
    Flux<List<NotificationFullView>> streamForUser_all(Long userId);
}
