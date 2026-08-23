package hr.algebra.gamearena.api.repository.notification;

import hr.algebra.gamearena.api.orm.postgres.notification.NotificationPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface INotificationPostgreSQLRepo extends JpaRepository<NotificationPostgres, Long> {
    List<NotificationPostgres> findByRecipientUserIdAndReadFalseOrderByCreatedAtDesc(Long recipientUserId);
    List<NotificationPostgres> findByRecipientUserIdOrderByCreatedAtDesc(Long recipientUserId);
    Integer countByRecipientUserIdAndReadFalse(Long recipientUserId);
    boolean existsByIdAndRecipientUserId(Long id, Long recipientUserId);
}
