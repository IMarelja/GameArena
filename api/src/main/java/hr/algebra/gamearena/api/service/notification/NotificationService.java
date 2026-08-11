package hr.algebra.gamearena.api.service.notification;

import hr.algebra.gamearena.api.dto.notification.NotificationCountView;
import hr.algebra.gamearena.api.dto.notification.NotificationCreateRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationEditRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationMinimalView;
import hr.algebra.gamearena.api.exceptions.extenders.InvalidVariableException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.model.notification.NotificationSave;
import hr.algebra.gamearena.api.model.notification.NotificationUpdate;
import hr.algebra.gamearena.api.repository.notification.INotificationRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements INotificationService{
    private final INotificationRepo notificationRepo;

    private String notificationNotFound(Long id){
        return "Notification with id " + id + " not found";
    }

    public NotificationService(INotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    @Override
    public Optional<NotificationMinimalView> findById(Long id) {
        return Optional.of(notificationRepo.findById(id)
                .map(NotificationMinimalView::fromNotification)
                .orElseThrow(() -> new NotFoundException( notificationNotFound(id) )));
    }

    @Override
    public List<NotificationMinimalView> findAllByUserIdUnreadByDescendingDate(Long userId) {
        return notificationRepo.findUnreadByUserIdByDescendingDate(userId)
                .stream()
                .map(NotificationMinimalView::fromNotification)
                .toList();
    }

    @Override
    public List<NotificationMinimalView> findAllByUserIdByDescendingDate(Long userId) {
        return notificationRepo.findAllByUserIdByDescendingDate(userId)
                .stream()
                .map(NotificationMinimalView::fromNotification)
                .toList();
    }

    @Override
    public NotificationCountView countUnreadByUserId(Long userId) {
        return NotificationCountView.fromNotification(notificationRepo.countUnreadByUserIdByDescendingDate(userId));
    }

    @Override
    public NotificationMinimalView create(NotificationCreateRequest request) {
        if ((request.getReferenceId() == null) != (request.getReferenceType() == null)) {
            throw new InvalidVariableException("referenceId and referenceType must both be provided or both be omitted");
        }

        var notificationSave = new NotificationSave();
        notificationSave.setType(request.getType());
        notificationSave.setRecipientUserId(request.getRecipientUserId());
        notificationSave.setReferenceId(request.getReferenceId());
        notificationSave.setReferenceType(request.getReferenceType());

        return NotificationMinimalView.fromNotification(notificationRepo.save(notificationSave));
    }

    @Override
    public Optional<NotificationMinimalView> update(Long id, NotificationEditRequest request) {
        var notificationUpdate = new NotificationUpdate();
        notificationUpdate.setRead(request.getRead());

        return Optional.of(NotificationMinimalView.fromNotification(notificationRepo.update(id, notificationUpdate)));
    }

    @Override
    public boolean deleteById(Long id){
        return notificationRepo.delete(id);
    }

    @Override
    public boolean doesUserOwnNotification(Long userId, Long notificationId) {
        return notificationRepo.doesUserOwnThisNotification(userId, notificationId);
    }
}
