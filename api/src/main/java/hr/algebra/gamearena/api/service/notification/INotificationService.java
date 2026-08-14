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

    Flux<NotificationUnreadAndCountView> streamForUserUnreadAndCount(Long userId);
    Flux<NotificationUnreadCountView> streamToUserUnreadCount(Long userId);
    Flux<List<NotificationFullView>> streamForUserAll(Long userId);
}
