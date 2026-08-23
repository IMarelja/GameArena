package hr.algebra.gamearena.api.repository.notification;

import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.model.notification.Notification;
import hr.algebra.gamearena.api.model.notification.NotificationSave;
import hr.algebra.gamearena.api.model.notification.NotificationUpdate;
import hr.algebra.gamearena.api.orm.postgres.notification.NotificationPostgres;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationPostgresRepo implements INotificationRepo{
    private final INotificationPostgreSQLRepo notificationPostgreSQLRepo;

    public NotificationPostgresRepo(INotificationPostgreSQLRepo notificationPostgreSQLRepo) {
        this.notificationPostgreSQLRepo = notificationPostgreSQLRepo;
    }

    @Override
    public List<Notification> findAll() {
        return notificationPostgreSQLRepo.findAll()
                .stream()
                .map(Notification::fromNotificationPostgres)
                .toList();
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationPostgreSQLRepo.findById(id)
                .map(Notification::fromNotificationPostgres);
    }

    @Override
    public List<Notification> findUnreadByUserIdByDescendingDate(Long id) {
        return notificationPostgreSQLRepo.findByRecipientUserIdAndReadFalseOrderByCreatedAtDesc(id)
                .stream()
                .map(Notification::fromNotificationPostgres)
                .toList();
    }

    @Override
    public List<Notification> findAllByUserIdByDescendingDate(Long id) {
        return notificationPostgreSQLRepo.findByRecipientUserIdOrderByCreatedAtDesc(id)
                .stream()
                .map(Notification::fromNotificationPostgres)
                .toList();
    }

    @Override
    public Integer countUnreadByUserIdByDescendingDate(Long id) {
        return notificationPostgreSQLRepo.countByRecipientUserIdAndReadFalse(id);
    }

    @Override
    public boolean doesUserOwnThisNotification(Long userId, Long notificationId) {
        return notificationPostgreSQLRepo.existsByIdAndRecipientUserId(notificationId, userId);
    }

    @Override
    public Notification save(NotificationSave save) {
        var notificationPostgres = new NotificationPostgres().fromNotificationSave(save);
        var savedNotification = notificationPostgreSQLRepo.save(notificationPostgres);
        return Notification.fromNotificationPostgres(savedNotification);
    }

    @Override
    public Notification update(Long id, NotificationUpdate update) {
        var notificationPostgres = notificationPostgreSQLRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found with id: " + id))
                .fromNotificationUpdate(update);

        var updatedNotification = notificationPostgreSQLRepo.save(notificationPostgres);
        return Notification.fromNotificationPostgres(updatedNotification);
    }

    @Override
    public void delete(Long id) {
        if (notificationPostgreSQLRepo.existsById(id)) {
            notificationPostgreSQLRepo.deleteById(id);
        }else{
            throw new NotFoundException("Notification not found with id: " + id);
        }
    }
}
